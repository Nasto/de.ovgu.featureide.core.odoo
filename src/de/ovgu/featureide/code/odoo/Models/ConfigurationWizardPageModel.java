package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;

public class ConfigurationWizardPageModel {
	
	/*
	 * "and": conjunction
	 * "xor": alternative
	 */
	public enum logicalOperator{and, alternative}
	
	public ConfigurationWizardPageModel(String pageName,String shortDescription, String question, ArrayList<SimpleFeature> features, logicalOperator featureOperator){
		this.pageName = pageName;
		this.shortDescription = shortDescription;
		this.question = question;
		this.features = features;
		this.featureOperator = featureOperator;
	}
	
	public ArrayList<SimpleFeature> features;

	public String shortDescription;
	
	public String question;
	
	public String pageName;

	public logicalOperator featureOperator;	
}
