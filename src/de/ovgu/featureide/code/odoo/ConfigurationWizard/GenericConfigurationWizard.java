package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.ovgu.featureide.code.odoo.Models.ConfigurationSection;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.Models.SimpleFeature;


public class GenericConfigurationWizard extends WizardPage {
	  private Composite container;
	  private ConfigurationWizardPageModel pageModel;

	  public GenericConfigurationWizard(ConfigurationSection section,ConfigurationWizardPageModel ConfigurationWizardPage) {
		super("");
		pageModel = ConfigurationWizardPage;	    
	    setTitle(section.getName());
	    setDescription(section.getDescripton());
	  }

	  @Override
	  public void createControl(Composite parent) {
	    container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 1;
	    
	    // Set up question text
	    Label questionLabel = new Label(container, SWT.NONE);
	    questionLabel.setText(pageModel.question);

	    // Set up Answers
	    switch(pageModel.featureOperator){
		    case and:{
		    	createCheckBoxGroup(pageModel.features);
		    	break;
		    }
		    case alternative:{
		    	createRadioButtonGroup(pageModel.features);
		    	break;
		    }
	    }
	    
	    // required to avoid an error in the system
	    setControl(container);
	  }
	  
	  	  
	  public ArrayList<SimpleFeature> getResult() {
	    return pageModel.features;
	  }
	  
	  private void createCheckBoxGroup(ArrayList<SimpleFeature> elements){
		//Group the radio buttons.
		  Composite composite = new Composite(container, SWT.NONE);
		  composite.setLayout(new RowLayout(SWT.VERTICAL));		  
		  for(SimpleFeature element: elements){			
		    Button btn = new Button(composite, SWT.CHECK);			
		    btn.setText(element.getName());
		    btn.setToolTipText(element.getDescription());
		    btn.setEnabled(element.isEnabled());
		    btn.setSelection(element.isChosen());
		  }
	  }
	  
	  private void createRadioButtonGroup(ArrayList<SimpleFeature> elements){
		  //Group the radio buttons.
		  Composite composite = new Composite(container, SWT.NONE);
		  composite.setLayout(new RowLayout(SWT.VERTICAL));		  
		  for(SimpleFeature element: elements){			
		    Button btn = new Button(composite, SWT.RADIO);			
		    btn.setText(element.getName());
		    btn.setToolTipText(element.getDescription());
		    btn.setEnabled(element.isEnabled());
		    btn.setSelection(element.isChosen());
		  }
	  }
	}
	 