package de.ovgu.featureide.code.odoo.Models;

public class ConfigurationWizardDependency {
	public String featureName;
	public boolean selection;
	
	public ConfigurationWizardDependency(String featureName) {
		this.featureName = featureName;
		selection = true;
	}
	
	public ConfigurationWizardDependency(String featureName, boolean selection) {
		this.featureName = featureName;
		this.selection = selection;
	}
}
