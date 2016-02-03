package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import de.ovgu.featureide.code.odoo.Models.ConfigurationSection;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.util.FolderParsing;
import de.ovgu.featureide.code.odoo.util.surveyConfigurationParser;


public class ConfigurationWizard extends Wizard {

  protected ArrayList<GenericConfigurationWizard> wizardPages;
  private int pageIndex;
  private String title;
  private ConfigurationWizardModel configurationModel;
  
  public ConfigurationWizard() {
    super();
    setNeedsProgressMonitor(true);
    configurationModel = surveyConfigurationParser.parseConfiguration(new File(FolderParsing.getCurrentProjectFolder()+"\\wizardConfiguration.xml"));
	setTitle(configurationModel.projectName);
	wizardPages = new ArrayList<GenericConfigurationWizard>();
  }
  
  private void setTitle(String title){
	this.title = "Configuration Wizard - " + title;
  }

  @Override
  public String getWindowTitle() {
    return title;
  }

  @Override
  public void addPages() {
	//Adds the first page
    pageIndex = 0;
	addPage(getWizardPageFromModel(pageIndex,configurationModel));	
  }
    
  @Override
	public boolean needsPreviousAndNextButtons() {
      return configurationModel.pages.size() > 1;
  }
  
  private GenericConfigurationWizard getWizardPageFromModel(int index, ConfigurationWizardModel configurationWizardModel ){
	  if (index >= configurationWizardModel.pages.size()) {
		// last page or page not found
	    return null;
	  }
      ConfigurationWizardPageModel page = configurationWizardModel.pages.get(index);
      // TODO: check if page has choices left to make, otherwise skip it
	  ConfigurationSection section = getSectionForPage(configurationWizardModel,page );
	  return new GenericConfigurationWizard(section, page);
  }
  
  @Override
	public IWizardPage getNextPage(IWizardPage page) {
	  // TODO: disable answers based on featureModel
	  GenericConfigurationWizard newPage = getWizardPageFromModel(pageIndex++,configurationModel );
	  if(newPage != null){
		  addPage(newPage);
	  }	  
      return newPage;
  }
  
  private ConfigurationSection getSectionForPage(ConfigurationWizardModel configurationModel, ConfigurationWizardPageModel page){
	  for(ConfigurationSection section: configurationModel.sections){
		  if(section.getId()==page.sectionId){
			  return section;
		  }
	  }	  
	  return new ConfigurationSection();
  }
  
  public boolean canFinish(){
	  return true;
  }
  

  @Override
  public boolean performFinish() {
	//TODO: create configuration
    return true;
  }
  
  
}
 