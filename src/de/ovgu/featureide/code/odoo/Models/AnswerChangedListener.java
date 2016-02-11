package de.ovgu.featureide.code.odoo.Models;

import java.util.ArrayList;

public interface AnswerChangedListener {
	void answersChangedEvent(ArrayList<ConfigurationWizardAnswer> answers);
}
