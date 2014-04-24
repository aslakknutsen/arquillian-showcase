package org.jboss.arquillian.showcase.universe.graphene;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConferenceForm {

	@Root
	private WebElement root;
	
	@FindBy(id = "create:conferenceName")
	private WebElement name;
	
	@FindBy(id = "create:conferenceLocation")
	private WebElement location;
	
	@FindBy(id = "create:conferenceDescription")
	private WebElement description;
	
	@FindBy(id = "create:conferenceId")
	private WebElement id;
	
	@FindBy(id = "create:conferenceRedirect")
	private WebElement redirect;
	
	@FindBy(id = "create:save")
	private WebElement save;
	
	public ConferenceForm setName(String name) {
		this.name.sendKeys(name);
		return this;
	}

	public ConferenceForm setLocation(String location) {
		this.location.sendKeys(location);
		return this;
	}

	public ConferenceForm setDescription(String description) {
		this.description.sendKeys(description);
		return this;
	}

	public ConferenceForm setId(String id) {
		this.id.sendKeys(id);
		return this;
	}
	
	public ConferenceForm redirect() {
		this.redirect.sendKeys("x");
		return this;
	}

	public void submit() {
		save.click();
	}
}
