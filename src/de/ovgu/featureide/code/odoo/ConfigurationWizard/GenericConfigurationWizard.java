package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.ovgu.featureide.code.odoo.Models.AnswerChangedListener;
import de.ovgu.featureide.code.odoo.Models.ConfigurationSection;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardAnswer;


public class GenericConfigurationWizard extends WizardPage {
	  private Composite container;
	  private ConfigurationWizardPageModel pageModel;
	  private AnswerChangedListener listener;

	  public GenericConfigurationWizard(ConfigurationSection section,ConfigurationWizardPageModel configurationWizardPage) {
		super("");
		pageModel = configurationWizardPage;	    
	    setTitle(section.getName());
	    setDescription(section.getDescripton());
	  }

	  @Override
	  public void createControl(Composite parent) {
		pageModel.visited = true;
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
		    	createCheckBoxGroup(pageModel.answers);
		    	break;
		    }
		    case alternative:{
		    	createRadioButtonGroup(pageModel.answers);
		    	break;
		    }
	    }	    
	    
	    // required to avoid an error in the system
	    setControl(container);
	    
	    if(listener != null){
	    	listener.answersChangedEvent(pageModel.answers);
	    }
	  }
	  
	  public void addEventListener(AnswerChangedListener listener){
		  this.listener = listener;
	  }
	  	  
	  public ArrayList<ConfigurationWizardAnswer> getResult() {
	    return pageModel.answers;
	  }
	  
	  public ConfigurationWizardPageModel getPageModel(){
		  return pageModel;
	  }
	  
	  private void createCheckBoxGroup(ArrayList<ConfigurationWizardAnswer> elements){
		//Group the radio buttons.
		  Composite composite = new Composite(container, SWT.NONE);
		  composite.setLayout(new RowLayout(SWT.VERTICAL));		  
		  SelectionListener selectionListener = new SelectionAdapter () {
		         public void widgetSelected(SelectionEvent event) {
			            Button button = ((Button) event.widget);
			            for(ConfigurationWizardAnswer a: pageModel.answers){
			            	if(a.getLabel()==button.getText()){
			            		a.setSelected(button.getSelection());
			            	}
			            }
			            if(listener != null){
			            	listener.answersChangedEvent(pageModel.answers);
			            	}
			         };
			      };  
		  for(ConfigurationWizardAnswer element: elements){			
		    Button btn = new Button(composite, SWT.CHECK);			
		    btn.setText(element.getLabel());
		    btn.setToolTipText(element.getDescription());
		    btn.setEnabled(element.isEnabled());
		    btn.setSelection(element.isSelected());
		    btn.setData(element);
		    btn.addSelectionListener(selectionListener);
		  }
	  }
	  
	  private void createRadioButtonGroup(ArrayList<ConfigurationWizardAnswer> elements){
		  //Group the radio buttons.
		  Composite composite = new Composite(container, SWT.NONE);
		  composite.setLayout(new RowLayout(SWT.VERTICAL));		
		  SelectionListener selectionListener = new SelectionAdapter () {
		         public void widgetSelected(SelectionEvent event) {
			            Button button = ((Button) event.widget);
			            for(ConfigurationWizardAnswer a: pageModel.answers){
			            	if(a.getLabel()==button.getText()){
			            		a.setSelected(button.getSelection());
			            	}
			            }
			            if(listener != null){
			            	listener.answersChangedEvent(pageModel.answers);
			            	}
			         };
			      };  
		  for(ConfigurationWizardAnswer element: elements){			
		    Button btn = new Button(composite, SWT.RADIO);			
		    btn.setText(element.getLabel());
		    btn.setToolTipText(element.getDescription());
		    btn.setEnabled(element.isEnabled());
		    btn.setSelection(element.isSelected());
		    btn.addSelectionListener(selectionListener);
		  }
	  }
	}
	 