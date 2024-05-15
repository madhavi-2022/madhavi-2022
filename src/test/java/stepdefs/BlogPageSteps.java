package stepdefs;

import ApplicationPages.Blogpage;
import io.cucumber.java.en.Then;

public class BlogPageSteps {
    private Blogpage blogPage;

    public BlogPageSteps() {
        this.blogPage = new Blogpage();
    }
    
    @Then("^User verify that Blog page title is displayed$")
    public void blogPageIsDisplayed() throws Exception {
        this.blogPage.verifyPageTitle();
    }
    @Then("I verify that page title displayed as {string}")
    public void PageIsDisplayed(String pagetitle) throws Exception {
        this.blogPage.verifyPageTitles(pagetitle);
    }
}
