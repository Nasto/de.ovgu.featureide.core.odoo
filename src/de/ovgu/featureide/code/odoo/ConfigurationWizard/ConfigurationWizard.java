package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.ovgu.featureide.code.odoo.Models.AnswerChangedListener;
import de.ovgu.featureide.code.odoo.Models.ConfigurationSection;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardAnswer;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.util.FolderParsing;
import de.ovgu.featureide.code.odoo.util.surveyConfigurationParser;


public class ConfigurationWizard extends Wizard {

  private GenericConfigurationWizard currentWizardPageModel;
  private String title;
  private ConfigurationWizardModel configurationModel;
  private boolean isCheck;
  private Button nextButton;
  
  public ConfigurationWizard() {
    super();
    isCheck = true;
    setNeedsProgressMonitor(true);
    configurationModel = surveyConfigurationParser.parseConfiguration(new File(FolderParsing.getCurrentProjectFolder()+"\\wizardConfiguration.xml"));
	setTitle(configurationModel.projectName);
  }
  
  public void passNextButton(Button nextButton){
	  this.nextButton = nextButton;
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
	  
	  getNextPage(null);
  }
    
  @Override
	public boolean needsPreviousAndNextButtons() {
      return configurationModel.pages.size() > 1;
  }
  
  private GenericConfigurationWizard getWizardPageFromModel(GenericConfigurationWizard currentPage, ConfigurationWizardModel configurationWizardModel ){
	  ConfigurationWizardPageModel page = null;
	  if(currentPage == null){
		  //get the first Page
		  page = configurationWizardModel.pages.get(0);
	  }else{		  
		  ArrayList<ConfigurationWizardAnswer> results = currentPage.getResult();
		  for (ConfigurationWizardAnswer answer : results){
			  if(answer.isSelected() && answer.hasNextPageId()){
				  //potential next page
				  if(configurationWizardModel.getPage(answer.getNextPageId()) != null && !configurationWizardModel.getPage(answer.getNextPageId()).visited){
					  // PageId is valid and page was not already visited		
					  page = configurationWizardModel.getPage(answer.getNextPageId());
					  break;
				  }
			  }
		  }
		  if(page == null){
			  if(isCheck){
				  //Check if there could be a next Page
				  for (ConfigurationWizardAnswer answer : results){
					  if(answer.hasNextPageId()){
						  //potential next page
						  if(configurationWizardModel.getPage(answer.getNextPageId()) != null && !configurationWizardModel.getPage(answer.getNextPageId()).visited){
							  // PageId is valid and page was not already visited		
							  page = configurationWizardModel.getPage(answer.getNextPageId());
							  break;
						  }				  
					  }
				  }
			  }else{
				  checkNextButton(currentPage.getResult());
			  }
			  if(page==null){
				  return null;
			  }			  
		  }
	  }
	  ConfigurationSection section = getSectionForPage(configurationWizardModel,page );
	  return new GenericConfigurationWizard(section, page);
  }
  
  private void checkNextButton(ArrayList<ConfigurationWizardAnswer> answers){
	  nextButton.setEnabled(false);
		for (ConfigurationWizardAnswer answer : answers){
			  if(answer.isSelected() && answer.hasNextPageId()){
				  nextButton.setEnabled(true);
				  break;
			  }					  
		  }
  }
  
  @Override
	public IWizardPage getNextPage(IWizardPage page) {
	  // TODO: disable answers based on featureModel
	  isCheck = !isCheck;
	  GenericConfigurationWizard newPage = getWizardPageFromModel(currentWizardPageModel,configurationModel );
	  if(newPage != null && !isCheck){
		  newPage.addEventListener(new AnswerChangedListener() {			
			@Override
			public void answersChangedEvent(ArrayList<ConfigurationWizardAnswer> answers) {
				checkNextButton(answers);
			}
		});
		  currentWizardPageModel = newPage;
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
 