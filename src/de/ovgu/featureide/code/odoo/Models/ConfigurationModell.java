package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;
import java.util.Map;

import javax.naming.directory.InvalidAttributesException;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;

public class ConfigurationModell {
	private FeatureModel featureModel;
	private Map<FeatureTopic, String> FeatureTopicDescription;
	private ArrayList<FeatureTopic> featureTopicOrder = new ArrayList<FeatureTopic>();
	
	public ConfigurationModell(FeatureModel featureModel) throws Exception{
		if(featureModel == null){
			throw new InvalidAttributesException("A FeatureModel must be set.");
		}
		this.featureModel = featureModel;
	}

	/*
	 * Adds a description to a feature.
	 */
	public void setFeatureDescription(Feature feature, String description){
		if(checkFeatureInput(feature)){
			return;
		}		
		FeatureTopicDescription.put(new FeatureTopic(feature), description);
	}
	
	/*
	 * Adds a description to a feature.
	 */
	public void setFeatureDescription(FeatureTopic featureTopic, String description){
		if(checkFeatureTopicInput(featureTopic)){
			return;
		}		
		FeatureTopicDescription.put(featureTopic, description);
	}
	
	/*
	 * Retrieves the description of a feature.
	 */
	public String getFeatureDescription(Feature feature){
		if(checkFeatureInput(feature)){
			return null;
		}
		if(!FeatureTopicDescription.containsKey(new FeatureTopic(featureModel.getFeature(feature.getName())))){
			return null;
		}		
		return FeatureTopicDescription.get(new FeatureTopic(featureModel.getFeature(feature.getName())));
	}
	
	/*
	 * Retrieves the description of a featuretopic.
	 */
	public String getFeatureDescription(FeatureTopic featureTopic){
		if(checkFeatureTopicInput(featureTopic)){
			return null;
		}
		if(!FeatureTopicDescription.containsKey(featureTopic)){
			return null;
		}		
		return FeatureTopicDescription.get(featureTopic);
	}
	
	/*
	 * Adds a feature to the end of the feature Order.
	 */
	public void addFeatureOrder(Feature feature){
		if(checkFeatureInput(feature)){
			return;
		}
		featureTopicOrder.add(new FeatureTopic(feature));
	}
	
	/*
	 * Adds a feature to the end of the feature Order.
	 */
	public void addFeatureOrder(FeatureTopic featureTopic){
		if(checkFeatureTopicInput(featureTopic)){
			return;
		}
		featureTopicOrder.add(featureTopic);
	}
		
	/*
	 * Swaps the feature with the previous one.
	 */
	public void upFeatureInOrder(FeatureTopic featureTopic){
		if(checkFeatureTopicInput(featureTopic)){
			return;
		}
		if(featureTopicOrder.contains(featureTopic)){
			int indexToMove = featureTopicOrder.indexOf(featureTopic);
			if(indexToMove>0){
				FeatureTopic toMove = featureTopicOrder.get(indexToMove);
				featureTopicOrder.set(indexToMove, featureTopicOrder.get(indexToMove-1));
				featureTopicOrder.set(indexToMove-1, toMove);
			}
		}
	}
	
	/*
	 * Swaps the feature with the following one.
	 */
	public void downFeatureInOrder(FeatureTopic featureTopic){
		if(checkFeatureTopicInput(featureTopic)){
			return;
		}
		if(featureTopicOrder.contains(featureTopic)){
			int indexToMove = featureTopicOrder.indexOf(featureTopic);
			if(indexToMove<featureTopicOrder.size()-1){
				FeatureTopic toMove = featureTopicOrder.get(indexToMove);
				featureTopicOrder.set(indexToMove, featureTopicOrder.get(indexToMove+1));
				featureTopicOrder.set(indexToMove+1, toMove);
			}			
		}
	}
	
	
	private boolean checkFeatureTopicInput(FeatureTopic feature){
		//TODO: implement
		return true;
	}
	
	/*
	 * Checks if the featureModel is set, if the given feature is part of the featureModel and whether the feature is null.
	 */
	private boolean checkFeatureInput(Feature feature){
		return featureModel == null || feature == null || featureModel.getFeature(feature.getName()) == null;
	}
	
}