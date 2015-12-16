package de.ovgu.featureide.code.odoo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;

import de.ovgu.featureide.code.odoo.Config;
import de.ovgu.featureide.fm.core.*;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelWriter;

public class FeatureModelInterpreter {
	
	private static FeatureModel parseFolderStructure(File[] folders){
		ArrayList<String> folderNames = new ArrayList<String>();		
		for (File folder : folders){
			folderNames.add(folder.getName());			
		}
		
		folderNames = orderFolderNames(folderNames);
		
		FeatureModel fm = new FeatureModel();
		Feature f = new Feature(fm,"Odoo");
		fm.setRoot(f);
		
		for (String folder : folderNames)
			addFolderNameToFMRec(fm, folder);
		
		System.out.println(fm.toString());	
		
		return fm;
	}
	
	private static Feature addFolderNameToFMRec(FeatureModel fm, String FolderName){
		Feature existingFeature = fm.getFeature(FolderName);
		if(existingFeature!= null){
			//Feature already exists
			return existingFeature;
		}
		Feature newFeature = null;
		int interleavingDegree = interleavingDegree(FolderName);
		if(interleavingDegree == 0){
			Feature root = fm.getRoot();
			newFeature = new Feature(fm,FolderName);
			fm.addFeature(newFeature);
			root.addChild(newFeature);
		}else{
			String fullSubName = FolderName.substring(0,FolderName.lastIndexOf("_"));
			String subName = getSubName(FolderName);
			String name = FolderName.substring(FolderName.lastIndexOf("_")+1);
			
			
			existingFeature = addFolderNameToFMRec(fm,fullSubName);
			newFeature = new Feature(fm,name);
			
			//existingFeature = fm.getFeature(subName);
			//if(existingFeature == null){
				//The Name contains "_"
				//existingFeature = fm.getRoot();
				//newFeature = new Feature(fm,FolderName);
			//}else{
			//	newFeature = new Feature(fm,name);
			//}
					
			
			fm.addFeature(newFeature);
			existingFeature.addChild(newFeature);
		}
		return newFeature;
	}	
	
	private static int interleavingDegree(String Name){
		return Name.length() - Name.replace("_", "").length();
	}
	
	
	private static String getSubName(String name){
		int interleavingDegree = interleavingDegree(name);
		if(interleavingDegree == 0) return "";
		if(interleavingDegree == 1){
			return name.substring(0,name.lastIndexOf("_"));
		}
		return name.substring(name.lastIndexOf("_", name.lastIndexOf("_")-1)+1,name.lastIndexOf("_"));
	}
		
	private static void addConfigFileToFM(FeatureModel fm, File file){
		String fileString = readFile(file);
		String folderName = file.getParentFile().getName();
		String featureName = folderName.substring(folderName.lastIndexOf("_")+1);
		
		//get rid of the python comments
		String cleanString = fileString.replaceAll("(#+.*[\r\n]*)", "");

		cleanString = cleanString.replaceAll(",\\s*]", "]");	// , ] --> ]
		cleanString = cleanString.replaceAll(",\\s*}", "}");	// , } --> }
		cleanString = cleanString.replaceAll("\"\"\"", "\"");	// """ --> "
		cleanString = cleanString.replaceAll("\'\'\'", "\"");	// ''' --> "

		cleanString = cleanString.replaceAll("\\{[\r\n\\s]*\'", "{\"");	//{ ' --> {"
		cleanString = cleanString.replaceAll("\'[\r\n\\s]*\\}", "\"}");	//' } --> "}
		cleanString = cleanString.replaceAll(",[\r\n]*\\s*\'", ",\n\"");	//,\n' --> ,\n"
		cleanString = cleanString.replaceAll(":\\s*\'", ":\"");		//: ' --> :"
		cleanString = cleanString.replaceAll("\'\\s*:", "\":");		//' : --> ":
		cleanString = cleanString.replaceAll("\'\\s*,", "\",");			//' , --> ",
		
		cleanString = cleanString.replaceAll("\\[[\r\n\\s]*\'", "\\[\"");
		cleanString = cleanString.replaceAll("\'[\r\n\\s]*\\]", "\"\\]");
		
		
		Config fileConfig = null;
		try{
			fileConfig = Json.getGenson().deserialize(cleanString, Config.class);
		} catch(Exception e) {
			System.out.println(featureName + " config could not be deserialized:\n" + e.getMessage());
			return;
		}
		
		Feature existingFeature = fm.getFeature(featureName);
		if(existingFeature == null){
			
			throw new IllegalArgumentException(featureName + " could not be found in the given FeatureModel.");
		}
		System.out.println("Feature " + featureName + " found.");
		
		existingFeature.setDescription(fileConfig.description);
		/*
		List<Constraint> constraints = new ArrayList<>();
		for(String cstr : fileConfig.depends){
			Constraint tempCstr = new Constraint(fm, propNode)
		}
		*/
	}
	
	
	private static ArrayList<String> orderFolderNames(ArrayList<String> folderNames){
		ArrayList<String> result = new ArrayList<>();
		if(folderNames.size()==0) return result;
		int i = 0;
		while(result.size() != folderNames.size()){			
			result.addAll(getStringsContaining(folderNames,"_",i++));
		}
		return result;
	}
	
	private static ArrayList<String> getStringsContaining(ArrayList<String> totalStrings, String substring, int amount){
		ArrayList<String> result = new ArrayList<>();
		for (String string : totalStrings){
			int occurency = string.length() - string.replace(substring, "").length();
			if(occurency == amount){
				result.add(string);
			}
		}
		return result;
	}
	
	public static String createFeatureModel(){
		return createFeatureModel("");
	}
	
	public static String createFeatureModel(String path){
		String result = "Project Path:\r\n";
		File ProjectFolder;
		if(path == ""){
			ProjectFolder = FolderParsing.getCurrentProjectFolder();
			if(ProjectFolder == null) return "Please select a Project.";			
		}else
		{
			ProjectFolder = new File(path);
		}			
		result += ProjectFolder.toString();
		result += "\r\n\r\nAddons Folder:\r\n";
		
		String folderName = "addons";
		File addonFolder = FolderParsing.findFolderByName(ProjectFolder,folderName);
		if(addonFolder == null) return "There is no folder named '"+folderName+"' beneath this Path.";		
		result += addonFolder.toString();
		
		File[] addonFolders = FolderParsing.retrieveSubFolders(addonFolder);
		if(addonFolders == null) return "There are no folders beneath '"+folderName+"'.";		
		result += "\r\n\r\nFolders beneath '"+folderName+"': "+addonFolders.length+"\r\n";		
		
		String configFileName = "__openerp__.py";
		File[] configFiles = FolderParsing.retrieveSubFiles(addonFolders,configFileName);
		result += "\r\nFiles inside those Folders named '__openerp__.py': " + configFiles.length+"\r\n";				
		
		System.out.println(result);
    	
		FeatureModel fm = parseFolderStructure(addonFolders);
		
//		for(File file : configFiles){
//			addConfigFileToFM(fm, file);
//		}
		
		File xml = new File(ProjectFolder.getAbsolutePath() + "\\generatedModel.xml");
		new XmlFeatureModelWriter(fm).writeToFile(xml);
		
		//result += "Feature Model was created successfully";
		return result;
	}
	
	
	private static String readFile(File file) {    

        char[] buffer = null;    

        try {    
                BufferedReader bufferedReader = new BufferedReader( new FileReader(file));    

                buffer = new char[(int)file.length()];    

                int i = 0;    
                int c = bufferedReader.read();    

                while (c >= 0) {    
                    buffer[i++] = (char)c;    
                    c = bufferedReader.read();    
                }
                
                bufferedReader.close();

        } catch (IOException e) {    
            e.printStackTrace();    
        }    

        return new String(buffer);    
    }
}
