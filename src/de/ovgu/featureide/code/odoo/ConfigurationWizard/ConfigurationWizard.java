package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;

import de.ovgu.featureide.code.odoo.Models.AnswerChangedListener;
import de.ovgu.featureide.code.odoo.Models.ConfigurationSection;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardAnswer;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardDependency;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.util.FolderParsing;
import de.ovgu.featureide.code.odoo.util.surveyConfigurationParser;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.Selection;


public class ConfigurationWizard extends Wizard {

  private GenericConfigurationWizard currentWizardPageModel;
  private String title;
  private ConfigurationWizardModel configurationModel;
  private boolean isCheck;
  private Button nextButton;
  private FeatureModel featureModel;
  private Configuration configuration;
  
  
  public ConfigurationWizard(FeatureModel featureModel) {
    super();
    this.featureModel = featureModel;
    configuration = new Configuration(this.featureModel, true);
    
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
				  checkNextButtonState(currentPage.getResult());
			  }
			  if(page==null){
				  return null;
			  }			  
		  }
	  }
	  ConfigurationSection section = getSectionForPage(configurationWizardModel,page );
	  return new GenericConfigurationWizard(section, page);
  }
  
  private void checkNextButtonState(ArrayList<ConfigurationWizardAnswer> answers){
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
	  if(!isCheck && currentWizardPageModel != null){
		  propagate(currentWizardPageModel.getResult());
	  }
	  GenericConfigurationWizard newPage = getWizardPageFromModel(currentWizardPageModel,configurationModel );
	  if(newPage != null && !isCheck){
		  newPage.addEventListener(new AnswerChangedListener() {			
			@Override
			public void answersChangedEvent(ArrayList<ConfigurationWizardAnswer> answers) {
				checkNextButtonState(answers);
			}
		  });
		  handlePropagatedAnswers(newPage);
		  currentWizardPageModel = newPage;
		  addPage(newPage);
	  }
      return newPage;
  }
  
  private void handlePropagatedAnswers(GenericConfigurationWizard page){
	  for (ConfigurationWizardAnswer answer : page.getPageModel().answers){
		  boolean isEnabled = true;
		  int amount = 0;
		  for(ConfigurationWizardDependency dependency :answer.getDependencies() ){
			  if(configuration.getSelectedFeatureNames().contains(dependency.featureName)){
				  //This answer has been selected already
				  amount++;
				  isEnabled = false;
			  }
		  }
		  if(amount>0 && amount == answer.getDependencies().size()){
			 answer.setSelected(true); 
		  }
		  
		  answer.setEnabled(isEnabled);
	  }
  }
  
  private void propagate(ArrayList<ConfigurationWizardAnswer> answers){
	  for(ConfigurationWizardAnswer answer : answers)
	  {
		for(ConfigurationWizardDependency dependency : answer.getDependencies()){
			if(featureModel.getFeature(dependency.featureName) != null){
				//Feature exists in FM
				configuration.setManual(dependency.featureName, dependency.selection ? Selection.SELECTED : Selection.UNSELECTED);
				configuration.update();				
			}else{
				System.err.println("Warning, feature '"+dependency.featureName+"' does not exist in FeatureModel");
			}
		}
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
	propagate(currentWizardPageModel.getResult());
	FolderParsing.writeToFile(configuration.toString());
    return true;
  }
  
  
}
 