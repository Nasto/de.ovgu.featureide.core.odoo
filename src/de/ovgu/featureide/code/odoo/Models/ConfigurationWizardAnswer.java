package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;

public class ConfigurationWizardAnswer {
	private String label;
	private String description;
	private boolean enabled;
	private boolean selected;
	private ArrayList<ConfigurationWizardDependency> dependencies;
	private int nextPageId;
	private boolean hasNextPageId;
	
	public ConfigurationWizardAnswer(String label, String description){
		this.label = label;
		this.description = description;
		enabled = true;
		this.selected = false;
		dependencies = new ArrayList<ConfigurationWizardDependency>();
		nextPageId = 0;
		hasNextPageId = false;
	}
	
	public void addDependency(ConfigurationWizardDependency dependency){
		dependencies.add(dependency);
	}
	
	public ArrayList<ConfigurationWizardDependency> getDependencies(){
		return dependencies;
	}
	
	public void setNextPageId(int id){
		nextPageId = id;
		hasNextPageId = true;
	}
	
	public boolean hasNextPageId(){
		return hasNextPageId;
	}
	
	public int getNextPageId(){
		return nextPageId;
	}
	
	public void setSelected(boolean state){
		selected = state;
	}

	public void setEnabled(boolean state){
		enabled = state;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
}
