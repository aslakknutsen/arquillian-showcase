package org.jboss.arquillian.showcase.universe.graphene;

import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.support.FindBy;

@Location("view/conference/create.jsf")
public class ConferencePage {

	@FindBy(id = "create")
	private ConferenceForm form;
	
	public ConferenceForm getForm() {
		return form;
	}
}
