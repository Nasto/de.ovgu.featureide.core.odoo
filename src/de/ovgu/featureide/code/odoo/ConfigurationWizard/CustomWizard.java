package de.ovgu.featureide.code.odoo.ConfigurationWizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class CustomWizard extends WizardDialog {

	public CustomWizard(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		setHelpAvailable(false);
	}

	@Override
	public void createButtonsForButtonBar(Composite parent){
		super.createButtonsForButtonBar(parent);
	    Button backButton = getButton(IDialogConstants.BACK_ID);
	    backButton.setVisible(false);
	}
}
