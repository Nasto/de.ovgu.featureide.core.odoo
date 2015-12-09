package de.ovgu.featureide.code.odoo.util;

import java.io.File;
import java.util.ArrayList;

import de.ovgu.featureide.fm.core.*;

public class FeatureModelInterpreter {
	
	private static void parseFolderStructure(File[] folders){
		ArrayList<String> folderNames = new ArrayList<String>();		
		for (File folder : folders){
			folderNames.add(folder.getName());			
		}
		
		folderNames = orderFolderNames(folderNames);
		
		for (String folder : folderNames)
			System.out.println(folder);		
		
		FeatureModel fm = new FeatureModel();
		Feature f = new Feature(fm,"Odoo");
		fm.setRoot(f);
		Feature child = new Feature(fm,"kind");
		fm.addFeature(child);
		f.addChild(child);
		System.out.println(fm.toString());
		
	}
	
	private static void addFolderNameToFM(FeatureModel fm, String FolderName){
		int interleavingDegree = FolderName.length() - FolderName.replace("_", "").length();
		if(interleavingDegree == 0){
			Feature root = fm.getRoot();
			Feature newFeature = new Feature(fm,FolderName);
			fm.addFeature(newFeature);
			root.addChild(newFeature);
		}
		
	}
	
	private static void addFolderNameToFMRec(FeatureModel fm, String FolderName){
		//Hier bin ich gerade..
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
    	
		parseFolderStructure(addonFolders);
		
		//result += "Feature Model was created successfully";
		return result;
	}
}
