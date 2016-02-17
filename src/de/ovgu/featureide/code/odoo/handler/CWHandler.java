package de.ovgu.featureide.code.odoo.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.widgets.Display;

import de.ovgu.featureide.code.odoo.ConfigurationWizard.ConfigurationWizard;
import de.ovgu.featureide.code.odoo.ConfigurationWizard.CustomWizard;
import de.ovgu.featureide.code.odoo.util.FolderParsing;
import de.ovgu.featureide.fm.core.FeatureModel;



public class CWHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		FeatureModel fm = FolderParsing.getFirstFeatureModel();
		CustomWizard cw  = new CustomWizard(Display.getDefault().getActiveShell(),new ConfigurationWizard(fm));
		cw.open();
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
