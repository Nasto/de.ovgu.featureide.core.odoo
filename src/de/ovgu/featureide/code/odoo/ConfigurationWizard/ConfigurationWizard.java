package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.jface.wizard.Wizard;

import de.ovgu.featureide.code.odoo.Models.ConfigurationSection;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel.logicalOperator;
import de.ovgu.featureide.code.odoo.Models.SimpleFeature;
import de.ovgu.featureide.code.odoo.util.FolderParsing;
import de.ovgu.featureide.code.odoo.util.surveyConfigurationParser;


public class ConfigurationWizard extends Wizard {

  protected ArrayList<GenericConfigurationWizard> wizardPages;
  private String title;
  
  public ConfigurationWizard() {
    super();
    setNeedsProgressMonitor(true);
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
	ConfigurationWizardModel configurationModel = surveyConfigurationParser.parseConfiguration(new File(FolderParsing.getCurrentProjectFolder()+"\\wizardConfiguration.xml"));
	setTitle(configurationModel.projectName);
	wizardPages = new ArrayList<GenericConfigurationWizard>();
	for(ConfigurationWizardPageModel page :configurationModel.pages){
		ConfigurationSection section = getSectionForPage(configurationModel, page);
		GenericConfigurationWizard newPage = new GenericConfigurationWizard(section, page);
		wizardPages.add(newPage);
		addPage(newPage);
	}
	
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
 