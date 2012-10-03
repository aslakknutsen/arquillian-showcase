package org.jboss.arquillian.showcase.universe.graphene;

import org.openqa.selenium.support.FindBy;

public class ConferencePage {

	@FindBy(id = "create")
	private ConferenceForm form;
	
	public ConferenceForm getForm() {
		return form;
	}
}
