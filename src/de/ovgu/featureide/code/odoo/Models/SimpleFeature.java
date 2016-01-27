package de.ovgu.featureide.code.odoo.Models;

public class SimpleFeature {
	private String name;
	private String description;
	private boolean enabled;
	private boolean chosen;
	
	public SimpleFeature(String name, String description){
		this.name = name;
		this.description = description;
		enabled = true;
		chosen = false;
	}
	
	public void setChosen(boolean state){
		chosen = state;
	}

	public void setEnabled(boolean state){
		enabled = state;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean isChosen(){
		return chosen;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
}
