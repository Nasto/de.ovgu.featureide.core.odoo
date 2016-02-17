package de.ovgu.featureide.code.odoo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelReader;

public class FolderParsing {

	/**
	 * Gets the currently selected Project.	 
	 */
	public static File getCurrentProjectFolder() {
		IProject project = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window != null)
	    {
	        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
	        Object firstElement = selection.getFirstElement();
	        if (firstElement instanceof IAdaptable)
	        {
	            project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
	        }
	    }
	    if(project != null){
	    	return new File(project.getLocation().toString());
	    }
	    return null;
	}
	
	public static FeatureModel getFirstFeatureModel(){
		File[] files = getFilesWithEnding("model.xml");
		if(files.length == 0) return null;
		FeatureModel fm = new FeatureModel();
		fm.xxxSetSourceFile(files[0]);
		XmlFeatureModelReader d = new XmlFeatureModelReader(fm);
		try {
			d.readFromFile(files[0]);
		} catch (FileNotFoundException | UnsupportedModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fm;
	}
	
	public static void writeToFile(String content){
		try {
			Date date = new Date() ;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
			File file = new File(FolderParsing.getCurrentProjectFolder().getAbsolutePath() + "\\"+ dateFormat.format(date)+".config");			
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(content);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File[] getFilesWithEnding(final String ending){
		File dir = getCurrentProjectFolder();
		File [] files = dir.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(ending);
		    }
		});

		for (File xmlfile : files) {
		    System.out.println(xmlfile);
		}
		return files;
	}
	
	/**
	 * Finds a Folder by its name beneath a given Folder recursively.
	 * @param folder
	 * @param name
	 * @return Folder with the given name or null if it could not be found.
	 */
	public static File findFolderByName(File folder, String name){
		File[] totalFiles = folder.listFiles();
		File result = null;
		for(File file : totalFiles){
			if(file.isDirectory()){
				if(file.getName().equals(name)){
					return file;
				}else
				{
					File potentialResult = findFolderByName(file,name);
					if( potentialResult!= null){
						result = potentialResult;
					};
				}
			}
		}
		return result;
	}
	
	/**
	 * Retrieves all folders contained inside the given folder
	 * @param folder
	 * @return An Array of Folders
	 */
	public static File[] retrieveSubFolders(File folder){
		File[] totalFiles = folder.listFiles();
		ArrayList<File> FolderList = new ArrayList<File>();
		for(File file : totalFiles){
			if(file.isDirectory()){
				FolderList.add(file);
			}
		}
		File[] result = new File[FolderList.size()];
		return FolderList.toArray(result);
	}
	
	/**
	 * Retrieves a File beneath a given Folder with a given Name.
	 * @param folder
	 * @param name
	 * @return File with specific name.
	 */
	private static File retrieveSubFiles(File folder, String name){
		File[] totalFiles = folder.listFiles();
		for(File file : totalFiles){
			if(file.isFile() && file.getName().equals(name)){
				return file;
			}
		}
		return null;
	}	
	
	/**
	 * Retrieves all files with a given name beneath the given folders.
	 * @param folders
	 * @param name
	 * @return Array of searched Files
	 */
	public static File[] retrieveSubFiles(File[] folders, String name){
		ArrayList<File> fileList = new ArrayList<File>();
		for(File folder : folders){
			fileList.add(retrieveSubFiles(folder, name));
		}
		File[] result = new File[fileList.size()];
		return fileList.toArray(result);
	}
}
