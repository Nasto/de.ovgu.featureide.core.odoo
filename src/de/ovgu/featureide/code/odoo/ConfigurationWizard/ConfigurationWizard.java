package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.Wizard;

import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel.logicalOperator;
import de.ovgu.featureide.code.odoo.Models.SimpleFeature;


public class ConfigurationWizard extends Wizard {

  protected GenericConfigurationWizard pageOne;
  protected GenericConfigurationWizard pageTwo;
  protected GenericConfigurationWizard pageThree;
  private String title;
  
  public ConfigurationWizard() {
    super();
    this.title = "Configuration Wizard - "+"GPL";
    setNeedsProgressMonitor(true);
  }

  @Override
  public String getWindowTitle() {
    return title;
  }

  @Override
  public void addPages() {
	  ArrayList<SimpleFeature> Featurelist1 = new ArrayList<SimpleFeature>();
		Featurelist1.add(new SimpleFeature("Weighted","This is a weighted Graph"));
		Featurelist1.add(new SimpleFeature("Unweighted","This is an unweighted graph"));
		ConfigurationWizardPageModel pageModel1 = new ConfigurationWizardPageModel("Graph type","Configure the type of graph you want.", "Is your graph weighted or unweighted?",
				Featurelist1,logicalOperator.alternative);
		
		ArrayList<SimpleFeature> Featurelist2 = new ArrayList<SimpleFeature>();
		Featurelist2.add(new SimpleFeature("Directed",""));
		Featurelist2.add(new SimpleFeature("Undirected",""));
		ConfigurationWizardPageModel pageModel2 = new ConfigurationWizardPageModel("Graph type","Configure the type of graph you want.", "Is your graph directed or undirected?",
				Featurelist2,logicalOperator.alternative);
		
		ArrayList<SimpleFeature> Featurelist3 = new ArrayList<SimpleFeature>();
		Featurelist3.add(new SimpleFeature("Number","Something with numbers.."));
		Featurelist3.add(new SimpleFeature("Connected",""));
		SimpleFeature f = new SimpleFeature("Cycle","");
		f.setEnabled(false);
		f.setChosen(true);
		Featurelist3.add(f);
		Featurelist3.add(new SimpleFeature("MSTPrim",""));
		Featurelist3.add(new SimpleFeature("MSTKruskal",""));
		ConfigurationWizardPageModel pageModel3 = new ConfigurationWizardPageModel("Algorithms","Set the algorithms you want to use on your graph.", "Select the algorithms that you want to use.",
				Featurelist3,logicalOperator.and);
		
	pageOne = new GenericConfigurationWizard(pageModel1);
	pageTwo = new GenericConfigurationWizard(pageModel2);
	pageThree = new GenericConfigurationWizard(pageModel3);
    addPage(pageOne);
    addPage(pageTwo);
    addPage(pageThree);
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
 