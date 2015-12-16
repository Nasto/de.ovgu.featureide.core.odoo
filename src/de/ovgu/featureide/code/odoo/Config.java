package de.ovgu.featureide.code.odoo;

/**
 * A class to hold the values which parsed from JSON configs.
 * 
 * @author Stephan Dörfler
 *
 */
public class Config {

	public String name;
	public String version;
	public String website;
	public String category;
	public String description;
	public String author;
	public String[] depends;
	public String[] data;
	public String[] qweb;
	public String[] demo;
	public String[] test;
	public int sequence;
	public boolean installable;
	public boolean auto_install;
	
	
}
