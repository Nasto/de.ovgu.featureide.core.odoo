package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;

import de.ovgu.featureide.fm.core.Feature;

public class FeatureTopic {
	private ArrayList<Feature> features = new ArrayList<Feature>();
	
	FeatureTopic(ArrayList<Feature> features){
		this.features = features;
	}
	
	FeatureTopic(Feature feature){		
		this.features.add(feature);
	}
	
	/*
	 * Adds a new feature to the list.
	 */
	public void addFeatureToTopic(Feature feature){
		if(features.contains(feature)) return;
		features.add(feature);
	}
	
	/*
	 * Removes an existing feature from the list.
	 */
	public void removeFeatureFromTopic(Feature feature){
		int index = features.indexOf(feature);
		if(index < 0) return;
		features.remove(index);
	}
}
