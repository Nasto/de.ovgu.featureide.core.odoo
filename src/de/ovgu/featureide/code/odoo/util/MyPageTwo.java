package de.ovgu.featureide.code.odoo.util;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MyPageTwo extends WizardPage {
  private Text text1;
  private Composite container;

  public MyPageTwo() {
    super("Naming Exceptions");
    setTitle("Naming Exceptions");
    setDescription("Add feature names, that have an underline '_' in it.");
    setControl(text1);
  }

  @Override
  public void createControl(Composite parent) {
    container = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    Label label1 = new Label(container, SWT.NONE);
    label1.setText("Features:");

    text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
    text1.setText("point_of_sale; claim_from_delivery; crm_demo");
    text1.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (!text1.getText().isEmpty()) {
          setPageComplete(true);
        }
      }

    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    text1.setLayoutData(gd);
    Label labelCheck = new Label(container, SWT.NONE);
    labelCheck.setText("Merge with existing Feature model");
    Button check = new Button(container, SWT.CHECK);
    check.setSelection(false);
    // required to avoid an error in the system
    setControl(container);
    setPageComplete(true);
  }

  public String getText1() {
    return text1.getText();
  }
}
 