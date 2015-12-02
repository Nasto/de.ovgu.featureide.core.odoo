package de.ovgu.featureide.code.odoo.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class FolderParsing {

	/**
	 * Gets the Current Project.
	 * 
	 */
	private static IProject getCurrentProject() {
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
		return project;
	}

	/**
	 * Retrieves an array of folder names of a given Folder.
	 * 
	 */
	private static ArrayList<IContainer> retrieveFolder(IContainer container) {
		IResource[] members;
		ArrayList<IContainer> folders = new ArrayList<IContainer>();
		if(container == null){
			//Log Error!
			return null;
		}
		try {
			members = container.members();			
			for (IResource member : members) {
				if (member instanceof IContainer) {					
					folders.add((IContainer) member);
					folders.addAll(retrieveFolder((IContainer) member));
				}
			}
			return folders;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folders;
	}
	
	private static ArrayList<IContainer> retrieveDirectSubFolder(IContainer container, String FolderName) {
		IResource[] members;
		ArrayList<IContainer> folders = new ArrayList<IContainer>();
		if(container == null){
			//Log Error!
			return null;
		}
		try {
			members = container.members();
			if(container.getName().equals(FolderName)){
				
				createFile(container,"hallo.xml");
				
				//Found it, add all of its folders to the list
				IResource[] subMembers = ((IContainer)container).members();
				for (IResource subMember : subMembers) {
					if (subMember instanceof IContainer) {
						folders.add((IContainer)subMember);
						}
					}
				return folders;
			}
			for (IResource member : members) {
				if (member instanceof IContainer) {				
					if(member.getName().equals(FolderName)){

						createFile((IContainer)member,"hallo.xml");
						//Found it, add all of its folders to the list
						IResource[] subMembers = ((IContainer)member).members();
						for (IResource subMember : subMembers) {
							if (subMember instanceof IContainer) {
								folders.add((IContainer)subMember);
								}
							}
						return folders;
					}else
					{
						folders.addAll(retrieveDirectSubFolder((IContainer)member, FolderName));
					}					
				}
			}
			return folders;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folders;
	}
	
	private static boolean createFile(IContainer container, String fileName){
		boolean success = false;
		// create File object
		//TODO: add created File to Project
		File stockDir = new File(container.getRawLocation().makeAbsolute()+ "/"+fileName);
		try {
			success = stockDir.createNewFile();
		} catch (IOException ioe) {
		     System.out.println("Error while Creating File in Java" + ioe);
		}
		return success;
	}
	
	
	/**
	 * Retrieves an array of folder names of a given Folder.
	 * 
	 */
	private static ArrayList<IFile> retrieveFiles(IContainer container) {
		IResource[] members;
		ArrayList<IFile> files = new ArrayList<IFile>();
		if(container == null){
			//Log Error!
			return null;
		}
		try {
			members = container.members();
			
			for (IResource member : members) {
				if (member instanceof IContainer) {		
					files.addAll(retrieveFiles((IContainer) member));
				} else if (member instanceof IFile) {
					files.add((IFile)member);
				}
			}
			return files;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return files;
	}
	
	public static String getFolderNames(){
		IProject currentProject = getCurrentProject();
		if (currentProject == null)
		{
			return "Please select a Project.";
		}
		ArrayList<IContainer> folders = retrieveFolder(currentProject);
		
		String result = "";
		for (IContainer folder : folders) {
			result += folder.getName()+ "\r\n";
		}
		return result;
	}
	
	public static String getFileNames(){
		IProject currentProject = getCurrentProject();
		if (currentProject == null)
		{
			return "Please select a Project.";
		}
		ArrayList<IFile> files = retrieveFiles(currentProject);
		
		String result = "";
		for (IFile file : files) {
			result += file.getName() + "\r\n";
		}
		return result;
	}
	
	public static String getFoldersBeneath(String FolderName){
		IProject currentProject = getCurrentProject();
		if (currentProject == null)
		{
			return "Please select a Project.";
		}		
		ArrayList<IContainer> folders = retrieveDirectSubFolder(currentProject, FolderName);
				
		String result = "";
		for (IContainer folder : folders) {
			result += folder.getRawLocation().makeAbsolute()+ "\r\n";
		}
		return result;
	}
}
