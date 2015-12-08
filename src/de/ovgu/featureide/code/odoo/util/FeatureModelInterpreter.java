package de.ovgu.featureide.code.odoo.util;

import java.io.File;
import java.util.ArrayList;

public class FeatureModelInterpreter {
	
	private static void parseFolderStructure(File[] folders){
		ArrayList<String> tempFolderNames = new ArrayList<String>();		
		for (File folder : folders){
			tempFolderNames.add(folder.getName());			
		}
		String[] folderNames = new String[tempFolderNames.size()];
		tempFolderNames.toArray(folderNames);
		
		for (String folder : folderNames)
		System.out.println(folder);		
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
