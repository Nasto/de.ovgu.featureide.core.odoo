package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;

public class ConfigurationWizardPageModel {
	
	/*
	 * "and": conjunction
	 * "xor": alternative
	 */
	public enum logicalOperator{and, alternative}
	
	public ConfigurationWizardPageModel(int sectionId, String question, ArrayList<SimpleFeature> features, logicalOperator featureOperator){
		this.sectionId = sectionId;
		this.question = question;
		this.features = features;
		this.featureOperator = featureOperator;
	}
	
	public ConfigurationWizardPageModel(){
		this.sectionId = 0;
		this.question = "";
		this.features = new ArrayList<SimpleFeature>();
		this.featureOperator = logicalOperator.and;
	}
	
	public ArrayList<SimpleFeature> features;
	
	public void addFeature(SimpleFeature feature){
		features.add(feature);
	}
	
	public String question;
	
	public logicalOperator featureOperator;	
	
	public int sectionId;
}
