package de.ovgu.featureide.code.odoo.Models;

public class ConfigurationSection {
	private int id;
	private String name;
	private String description;
	
	public ConfigurationSection(int id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public ConfigurationSection(){
		this.id = 0;
		this.name = "";
		this.description = "";
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescripton(){
		return description;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescripton(String description){
		this.description = description;
	}
}
