package de.ovgu.featureide.code.odoo.ConfigurationWizard;


import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.ovgu.featureide.code.odoo.util.FolderParsing;


public class ImportWizardPageOne extends WizardPage {
	  private Text path;
	  private Composite container;

	  public ImportWizardPageOne() {
	    super("Odoo Directory");
	    setTitle("Odoo Directory");
	    setDescription("Select the location of the Odoo project.");
	  }

	  @Override
	  public void createControl(Composite parent) {
	    container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 2;
	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("Odoo Directory:");

	    path = new Text(container, SWT.BORDER | SWT.SINGLE);
	    
	    File folder = FolderParsing.getCurrentProjectFolder();
	    if(folder != null){
	    	path.setText(folder.getAbsolutePath());
		    setPageComplete(true);
	    }else
	    { 
	    	path.setText("");
		    setPageComplete(false);	    	
	    }
	   
	    path.addKeyListener(new KeyListener() {

	      @Override
	      public void keyPressed(KeyEvent e) {
	      }

	      @Override
	      public void keyReleased(KeyEvent e) {
	        if (!path.getText().isEmpty()) {
	          setPageComplete(true);

	        }
	      }

	    });
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    path.setLayoutData(gd);
	    // required to avoid an error in the system
	    setControl(container);
	  }

	  public String getPath() {
	    return path.getText();
	  }
	}
	 