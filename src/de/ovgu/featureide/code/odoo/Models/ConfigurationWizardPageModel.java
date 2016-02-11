package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;

public class ConfigurationWizardPageModel {
	
	/*
	 * "and": conjunction
	 * "xor": alternative
	 */
	public enum logicalOperator{and, alternative}
	
	public ConfigurationWizardPageModel(int id, int sectionId, String question, ArrayList<ConfigurationWizardAnswer> features, logicalOperator featureOperator){
		this.sectionId = sectionId;
		this.question = question;
		this.answers = features;
		this.featureOperator = featureOperator;
		this.id = id;
		visited = false;
	}
	
	public ConfigurationWizardPageModel(){
		this.id = 0;
		this.sectionId = 0;
		this.question = "";
		this.answers = new ArrayList<ConfigurationWizardAnswer>();
		this.featureOperator = logicalOperator.and;
		visited = false;
	}
	
	public ArrayList<ConfigurationWizardAnswer> answers;
	
	public void addFeature(ConfigurationWizardAnswer answer){
		answers.add(answer);
	}
	
	public String question;
	
	public logicalOperator featureOperator;	
	
	public boolean visited;
	
	public int sectionId;
	
	public int id;
}
