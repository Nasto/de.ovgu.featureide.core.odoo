package de.ovgu.featureide.code.odoo.util;

import java.util.ArrayList;

import org.aspectj.org.eclipse.jdt.core.IJavaElement;
import org.aspectj.org.eclipse.jdt.core.IJavaProject;
import org.aspectj.org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

@SuppressWarnings("restriction")
public class FolderParsing {

	/**
	 * Gets the Current Project.
	 * 
	 */
	public static IProject getCurrentProject() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();

		ISelection selection = selectionService.getSelection();

		IProject project = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();

			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			} else if (element instanceof PackageFragmentRoot) {
				IJavaProject jProject = ((PackageFragmentRoot) element).getJavaProject();
				project = jProject.getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element).getJavaProject();
				project = jProject.getProject();
			}
		}
		return project;
	}

	/**
	 * Retrieves an array of folder names of a given Folder.
	 * 
	 */
	public static String processContainer(IContainer container) {
		IResource[] members;
		String result = "";
		ArrayList<IResource> files = new ArrayList<IResource>();
		if(container == null) return "No Container..";
		try {
			members = container.members();

			for (IResource member : members) {
				if (member instanceof IContainer) {
					result += processContainer((IContainer) member);
				} else if (member instanceof IFile) {
					files.add(member);
					result += "\r\n " + member.getName();
				}
			}
			return result;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
