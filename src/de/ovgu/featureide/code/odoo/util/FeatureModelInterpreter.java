package de.ovgu.featureide.code.odoo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.prop4j.And;
import org.prop4j.Implies;
import org.prop4j.Literal;

import com.owlike.genson.JsonBindingException;

import de.ovgu.featureide.code.odoo.Models.FeatureDataModell;
import de.ovgu.featureide.fm.core.*;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelWriter;

public class FeatureModelInterpreter {
	
	private static Map<Feature,Set<Feature>> uniqueConstraints;
	private static int featuresAdded = 0;
	
	
	/*
	 * Creates a FeatureModel out of the naming of the input folder names.
	 */
	private static FeatureModel parseFolderStructure(File[] folders, ArrayList<String> namingExceptions){
		ArrayList<String> folderNames = new ArrayList<String>();		
		for (File folder : folders){
			folderNames.add(folder.getName());			
		}
		
		folderNames = cleanNamingExceptions(folderNames, namingExceptions);
		folderNames = orderFolderNames(folderNames);
		
		FeatureModel fm = new FeatureModel();
		Feature f = new Feature(fm,"Odoo");
		fm.setRoot(f);
		
		for (String folder : folderNames){
			addFolderNameToFMRec(fm, folder, false);
		}
		return fm;
	}
	
	/*
	 * Some names contain an underline '_' witch would be interpreted as a seperator between two features. These are exchanged by a minus '-'.
	 */
	private static ArrayList<String> cleanNamingExceptions(ArrayList<String> folderNames, ArrayList<String> namingExceptions){
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(folderNames);
		for (int i = 0; i<result.size(); i++){
			if(namingExceptions.contains(result.get(i))){
				// Foldername is one of the exceptions
				result.set(i, result.get(i).replace("_", "-"));
			}else{
				for( String namingexception : namingExceptions){
					if(result.get(i).contains(namingexception)){
						// Part of the  Name is one of the exceptions
						result.set(i, result.get(i).replace(namingexception, namingexception.replace("_", "-")));
					}
				}
			}
		}		
		return result;
	}
	
	/*
	 * Some names contain an underline '_' witch would be interpreted as a seperator between two features. These are exchanged by a minus '-'.
	 */
	private static String cleanNamingExceptions(String folderName, ArrayList<String> namingExceptions){
		ArrayList<String> result = new ArrayList<String>();
		result.add(folderName);
		
		return cleanNamingExceptions(result, namingExceptions).get(0);
	}
	
	/*
	 * Adds a foldername(feature) to a FeatureModel and returns the newly added Feature. This function is recursive to add dependencies if
	 * the name is i.e. "root_parent_feature" --> "root_parent" + "feature" --> "root" + "parent" + "feature".
	 */
	private static Feature addFolderNameToFMRec(FeatureModel fm, String FolderName, boolean isRecursive){
		Feature existingFeature = fm.getFeature(FolderName);
		if(existingFeature!= null){
			//Feature already exists
			return existingFeature;
		}
		Feature newFeature = null;
		int interleavingDegree = interleavingDegree(FolderName);		
		if(interleavingDegree == 0){
			newFeature = new Feature(fm,FolderName);
			if(isRecursive){
				System.out.println("This feature had to be added: " + FolderName);
				newFeature.setAbstract(true);
			}
			Feature root = fm.getRoot();
			featuresAdded++;
			fm.addFeature(newFeature);
			root.addChild(newFeature);
		}else{
			String fullSubName = FolderName.substring(0,FolderName.lastIndexOf("_"));
			
			existingFeature = addFolderNameToFMRec(fm,fullSubName, true);
			
			featuresAdded++;
			newFeature = new Feature(fm,FolderName);
			if(isRecursive){
				System.out.println("This feature had to be added: " + FolderName);
				newFeature.setAbstract(true);
			}				
			fm.addFeature(newFeature);
			existingFeature.addChild(newFeature);
		}
		return newFeature;
	}	
	
	/*
	 * Returns the number of strings separated by an underline.
	 */
	private static int interleavingDegree(String Name){
		return Name.length() - Name.replace("_", "").length();
	}
	
	/*
	 * Adds the collected constraints.
	 */
	private static void addConstraints(FeatureModel fm){
		for(Map.Entry<Feature, Set<Feature>> entry : uniqueConstraints.entrySet()){
			ArrayList<Literal> literalList = new ArrayList<Literal>();
			for(Feature featureEntry : entry.getValue()){
				literalList.add(new Literal(featureEntry.getName()));
			}
			fm.addConstraint(new Constraint(fm,
							new Implies(
							new Literal(entry.getKey().getName()),
							new And(literalList)
							)
					));			
		}				
	}
	
	private static void addConfigFileToFM(FeatureModel fm, File file, ArrayList<String> namingExceptions){
		String fileString = readFile(file);
		String folderName = file.getParentFile().getName();
		
		String featureName = cleanNamingExceptions(folderName,namingExceptions);
				
		Feature existingFeature = fm.getFeature(featureName);
		if(existingFeature == null){
			throw new IllegalArgumentException(featureName + " could not be found in the given FeatureModel.");
		}
		
		//TODO: some errors left.
		//get rid of the python comments
		String cleanString = fileString.replaceAll("(#+.*[\r\n]*)", "");

		cleanString = cleanString.replaceAll(",\\s*]", "]");	// , ] --> ]
		cleanString = cleanString.replaceAll(",\\s*}", "}");	// , } --> }
		cleanString = cleanString.replaceAll("\"\"\"", "\"");	// """ --> "
		cleanString = cleanString.replaceAll("\'\'\'", "\"");	// ''' --> "

		cleanString = cleanString.replaceAll("\\{[\r\n\\s]*\'", "{\"");		//{ ' --> {"
		cleanString = cleanString.replaceAll("\'[\r\n\\s]*\\}", "\"}");		//' } --> "}
		cleanString = cleanString.replaceAll(",[\r\n]*\\s*\'", ",\n\"");	//,\n' --> ,\n"
		cleanString = cleanString.replaceAll(":\\s*\'", ":\"");				//: ' --> :"
		cleanString = cleanString.replaceAll("\'\\s*:", "\":");				//' : --> ":
		cleanString = cleanString.replaceAll("\'\\s*,", "\",");				//' , --> ",
		
		cleanString = cleanString.replaceAll("\\[[\r\n\\s]*\'", "\\[\"");
		cleanString = cleanString.replaceAll("\'[\r\n\\s]*\\]", "\"\\]");
		
		
		FeatureDataModell fileConfig = null;
		try{
			fileConfig = Json.getGenson().deserialize(cleanString, FeatureDataModell.class);
		} catch(JsonBindingException e) {
			System.err.println(featureName + " config could not be deserialized:\n" + e.getCause().getMessage());
			return;
		}		
		
		//add dependencies as constraints to the global map, if it's not the parent
		for(final String cstr : fileConfig.depends){
			if(!existingFeature.getParent().getName().equals(cstr)) {
				final String impliedFeatureName = cleanNamingExceptions(cstr,namingExceptions);
				final Feature impliedFeature = fm.getFeature(impliedFeatureName);
				if (impliedFeature == null) {
					System.err.println("feature " + impliedFeatureName + " not found");
					continue;
				}
				//Add constraints to map-collection
				if (uniqueConstraints.containsKey(existingFeature)){
					uniqueConstraints.get(existingFeature).add(impliedFeature);
				}else{
					uniqueConstraints.put(existingFeature, new HashSet<Feature>(){
						private static final long serialVersionUID = 1L;
					{
						add(impliedFeature);
					}});
				}				
			}
		}		
		
		String descText = "";
		
		if(fileConfig.name != null){
			descText += "Name:\n\t";
			descText += fileConfig.name;
		}
		
		if (fileConfig.version != null) {
			descText += "\nVersion:\n\t";
			descText += fileConfig.version;
		}
		
		if (fileConfig.website != null) {
			descText += "\nWebsite:\n\t";
			descText += fileConfig.website;
		}
		
		if (fileConfig.category != null) {
			descText += "\nCategory:\n\t";
			descText += fileConfig.category;
		}
		
		if (fileConfig.description != null) {
			descText += "\nDescription:\n\t";
			descText += fileConfig.description;
		}
		
		if (fileConfig.author != null) {
			descText += "\nAuthor:";
			if (fileConfig.author.getClass().equals(String.class)) {
				descText += fileConfig.author;
			} else {
				@SuppressWarnings("unchecked")
				Collection<String> authors = (Collection<String>) fileConfig.author;
				for(String auth : authors){
					descText += "\n\t" + auth;
				}
			}
		}
		
		if(fileConfig.depends != null){
			descText += "\nDepends:";
			for(String dep : fileConfig.depends){
				descText += "\n\t"+dep;
			}
		}
		
		if(fileConfig.data != null){
			descText += "\nData:";
			for(String data : fileConfig.data){
				descText += "\n\t"+data;
			}
		}
		
		if(fileConfig.qweb != null){
			descText += "\nQweb:";
			for(String qweb : fileConfig.qweb){
				descText += "\n\t"+qweb;
			}
		}
		
		if(fileConfig.demo != null){
			descText += "\nDemo:";
			for(String demo : fileConfig.demo){
				descText += "\n\t"+demo;
			}
		}
		
		if(fileConfig.test != null){
			descText += "\nTest:";
			for(String test : fileConfig.test){
				descText += "\n\t"+test;
			}
		}
		
		descText += "\nSequence:\n\t";
		descText += fileConfig.sequence;
		
		descText += "\nInstallable:\n\t";
		descText += fileConfig.installable;
		
		descText += "\nAuto_Install:\n\t";
		descText += fileConfig.auto_install;
		
		existingFeature.setDescription(descText);
	}
	
	/*
	 * Orders an array of Strings by the amount of underlines they contain.
	 */
	private static ArrayList<String> orderFolderNames(ArrayList<String> folderNames){
		ArrayList<String> result = new ArrayList<>();
		if(folderNames.size()==0) return result;
		int i = 0;
		while(result.size() != folderNames.size()){			
			result.addAll(getStringsContaining(folderNames,"_",i++));
		}
		return result;
	}
	
	/*
	 * Returns a List of all Strings in the input Array containing exactly <amount> of <substring>.
	 */
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
	
	/*
	 * Default constructor for no known feature Path, assuming the features are below the current project.
	 */
	public static String createFeatureModel(){
		return createFeatureModel("", new ArrayList<String>());
	}
	
	public static String createFeatureModel(String path, ArrayList<String> namingExceptions){
		String result = "";
		featuresAdded = 0;
		uniqueConstraints = new HashMap<Feature, Set<Feature>>();
		try{
			result = "Project Path:\r\n ";
			File ProjectFolder;
			if(path == ""){
				ProjectFolder = FolderParsing.getCurrentProjectFolder();
				if(ProjectFolder == null) return "Please select a Project.";			
			}else
			{
				ProjectFolder = new File(path);
			}			
			result += ProjectFolder.toString();
			result += "\r\n\r\nAddons Folder:\r\n ";
			
			String folderName = "addons";
			File addonFolder = FolderParsing.findFolderByName(ProjectFolder,folderName);
			if(addonFolder == null) return "There is no folder named '"+folderName+"' beneath this Path.";		
			result += addonFolder.toString();
			
			File[] addonFolders = FolderParsing.retrieveSubFolders(addonFolder);
			if(addonFolders == null) return "There are no folders beneath '"+folderName+"'.";		
			result += "\r\n\r\nFolders beneath '"+folderName+"': \t"+addonFolders.length+"\r\n";		
			
			String configFileName = "__openerp__.py";
			File[] configFiles = FolderParsing.retrieveSubFiles(addonFolders,configFileName);
			result += "Files inside those Folders named '__openerp__.py': \t" + configFiles.length;				
				    	
			FeatureModel fm = parseFolderStructure(addonFolders, namingExceptions);
			result += "\r\nFeatures Added: \t"+ featuresAdded + "\r\n ";
			
			for(File file : configFiles){
				addConfigFileToFM(fm, file,namingExceptions );
			}
			addConstraints(fm);
			Date date = new Date() ;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
			File xml = new File(ProjectFolder.getAbsolutePath() + "\\generatedModel_"+ dateFormat.format(date)+".xml");
			new XmlFeatureModelWriter(fm).writeToFile(xml);
			
			result += "\r\nFeature Model was created successfully";
			System.out.println(result);
			return result;
		}
		catch(Exception e){
			System.out.println(result);
			
			return "Error:  "+e.getLocalizedMessage() + "\r\n\r\nOutput so far: \r\n"+result;
		}
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
