package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;


public class ConfigurationWizardModel{
	
	public ArrayList<ConfigurationWizardPageModel> pages;
	
	public ArrayList<String> resultingFeatureNames;	

	public ArrayList<ConfigurationSection> sections;
	
	public String projectName;
	
	public ConfigurationWizardModel(){
		projectName = "";
		pages = new ArrayList<ConfigurationWizardPageModel>();
		sections = new ArrayList<ConfigurationSection>();
		resultingFeatureNames = new ArrayList<String>();
	}	
	
	public void setProjectName(String name){
		projectName = name;
	}
	
	public void addPage(ConfigurationWizardPageModel page){
		pages.add(page);
	}

	public void addSection(ConfigurationSection section){
		sections.add(section);
	}
	
	public ConfigurationWizardPageModel getPage(int id){
		for(ConfigurationWizardPageModel page: pages){
			if(page.id == id){
				return page;
			}
		}
		return null;
	}
}