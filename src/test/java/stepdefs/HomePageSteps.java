package stepdefs;

import java.io.IOException;
import java.util.Random;

import io.cucumber.java.en.When;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import ApplicationPages.Homepage;
import WebConnector.webconnector;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class HomePageSteps extends webconnector {
    private Homepage homePage;
	private String scenDesc;

    public HomePageSteps() {
        this.homePage = new Homepage();
    }
    
    @Before
	public void before(Scenario scenario) {
		this.scenDesc = scenario.getName();
		setUpDriver();
	}
    
    @After
    public void after(Scenario scenario){
    	closeDriver(scenario);
    }
	
	@BeforeStep
	public void beforeStep() throws InterruptedException {
		Thread.sleep(2000);
	}

    @Given("^User navigates to QATechTesting HomePage$")
    public void aUserNavigatesToHomePage() throws InvalidFormatException, IOException {
        this.homePage.goToHomePage();
    }
    @Given("^User navigates to Oasis app URL")
    public void aUserNavigatesToURL() throws InvalidFormatException, IOException {
        this.homePage.goToHomePage();
    }
    @When("I enter {string} text in {string} field")
    public void inputText(String txt, String element) throws InvalidFormatException, Exception {
    	
        this.homePage.inputText(element,txt);
        Thread.sleep(2000);
    }
    @When("I enter Random text in {string} field")
    public void inputRandomText(String element) throws InvalidFormatException, Exception {
        this.homePage.inputText(element,getSaltString());
        Thread.sleep(2000);
    }
    @When("I click on {string} link")
    public void clickLink(String element) throws InvalidFormatException, IOException {
        this.homePage.clickButton(element);
    }
    @And("I switch to nexttab")
    public void switchToNextWindow() throws InvalidFormatException, IOException {
        this.homePage.switchWindow();
    }
    @And("I verify {string} is Present")
    public void i_verify_element_ispresent(String element) throws InvalidFormatException, IOException {
        this.homePage.verifyElementExists(element);
    }
    
    @And("I log the {string} value to console")
    public void i_log_elementValue_toConsole(String element) throws InvalidFormatException, IOException {
        this.homePage.getElementText(element);
    }

   
    @When("I click on {string} button")
    public void clickButton(String element) throws InvalidFormatException, IOException {
        this.homePage.clickButton(element);
    }
    
    @When("I click on {string} checkBox")
    public void clickCheckBoxButton(String element) throws InvalidFormatException, IOException {
        this.homePage.clickButton(element);
    }
    @When("I click on {string} button if exists")
    public void clickButtonifExists(String element) throws InvalidFormatException, IOException {
        this.homePage.clickifExistButton(element);
    }
    @Then("^User verify that Blog Link is displayed$")
    public void googleLogoIsDisplayed() throws Exception {
        this.homePage.checkBlogLinkDisplayIfExists();
    }
    @Then("I select the {string} value from {string} Dropdown")
    public  void selectDropDown(String val, String dropdown){
        this.homePage.selectText(dropdown,val);
    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
