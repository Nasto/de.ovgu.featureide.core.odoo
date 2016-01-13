package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import org.eclipse.jface.wizard.Wizard;


public class ConfigurationWizard extends Wizard {

  protected ImportWizardPageOne pageOne;
  
  public ConfigurationWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  @Override
  public String getWindowTitle() {
    return "Configuration Wizard";
  }

  @Override
  public void addPages() {
	pageOne = new ImportWizardPageOne();
    addPage(pageOne);
  }
  
  public boolean canFinish(){
	  if(getContainer().getCurrentPage() == pageOne)
		  return false;
	  else 
		  return true;
  }
  

  @Override
  public boolean performFinish() {
	
    return true;
  }
}
 