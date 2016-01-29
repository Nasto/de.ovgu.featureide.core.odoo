package de.ovgu.featureide.code.odoo.util;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

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
