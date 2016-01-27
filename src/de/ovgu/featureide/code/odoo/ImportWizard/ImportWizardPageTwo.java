package de.ovgu.featureide.code.odoo.ImportWizard;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ImportWizardPageTwo extends WizardPage {
  private Text namingExceptions;
  private Composite container;

  public ImportWizardPageTwo() {
    super("Naming Exceptions");
    setTitle("Naming Exceptions");
    setDescription("Add feature names, that have an underline '_' in it. Seperate names by comma.");
    setControl(namingExceptions);
  }

  @Override
  public void createControl(Composite parent) {
    container = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    Label label1 = new Label(container, SWT.NONE);
    label1.setText("Features:");

    namingExceptions = new Text(container, SWT.BORDER | SWT.SINGLE);
    namingExceptions.setText("point_of_sale, claim_from_delivery, crm_demo");
    namingExceptions.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (!namingExceptions.getText().isEmpty()) {
          setPageComplete(true);
        }
      }

    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    namingExceptions.setLayoutData(gd);
    //Label labelCheck = new Label(container, SWT.NONE);
    //labelCheck.setText("Merge with existing Feature model");
    //Button check = new Button(container, SWT.CHECK);
    //check.setSelection(false);
    // required to avoid an error in the system
    setControl(container);
    setPageComplete(true);
  }

  public ArrayList<String> getNamingExceptions() {
    return new ArrayList<String>(Arrays.asList(namingExceptions.getText().split("\\s*,\\s*")));
  }
}
 