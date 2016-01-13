package de.ovgu.featureide.code.odoo.ImportWizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;

import de.ovgu.featureide.code.odoo.util.FeatureModelInterpreter;


public class ImportWizard extends Wizard {

  protected ImportWizardPageOne pageOne;
  protected ImportWizardPageTwo pageTwo;
  protected IWorkbenchWindow window;
  
  public ImportWizard(IWorkbenchWindow _window) {
    super();
    window = _window;
    setNeedsProgressMonitor(true);
  }

  @Override
  public String getWindowTitle() {
    return "Odoo Feature Model Builder";
  }

  @Override
  public void addPages() {
	  pageOne = new ImportWizardPageOne();
	  pageTwo = new ImportWizardPageTwo();
    addPage(pageOne);
    addPage(pageTwo);
  }
  
  public boolean canFinish(){
	  if(getContainer().getCurrentPage() == pageOne)
		  return false;
	  else 
		  return true;
  }
  

  @Override
  public boolean performFinish() {
	MessageDialog.openInformation(
			window.getShell(),
			"Odoo Feature Model Builder",
			FeatureModelInterpreter.createFeatureModel(pageOne.getPath(), pageTwo.getNamingExceptions()));
    return true;
  }
}
 