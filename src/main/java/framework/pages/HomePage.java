package framework.pages;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.time.Duration;

/**
 * Page object for the Home screen.
 */
public class HomePage extends BasePage {

    private final By screenRoot;
    private final By createBugButton;
    private final By viewBugsButton;
    private final By homeButton;

    /**
     * Constructs the HomePage.
     *
     * @param driver          Android driver for interactions (non-null)
     * @param explicitTimeout max wait per explicit condition
     */
    public HomePage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);

        this.screenRoot = resourceId("homePage");
        this.createBugButton = textElement("Create Bug");
        this.viewBugsButton = textElement("View Bugs");
        this.homeButton = textElement("Home");
    }

    /**
     * Verifies that the home page is displayed.
     *
     * @return true if home page is visible, false otherwise
     */
    public boolean verifyHomePage() {
        return assertOnPage(screenRoot);
    }

    /**
     * Clicks the "Create Bug" button to navigate to CreateBugPage.
     *
     * @return new instance of CreateBugPage
     */
    public CreateBugPage clickCreateBug() {
        click(createBugButton);
        return new CreateBugPage(driver, explicitTimeout);
    }

    /**
     * Clicks the "View Bugs" button to navigate to ViewBugsPage.
     */
    public void clickViewBugs() {
        click(viewBugsButton);
    }

    /**
     * Clicks the "Home" button to navigate to HomePage.
     */
    public void clickHome() {
        click(homeButton);
    }

    /**
     * Checks if the Create Bug button is visible.
     *
     * @return true if visible, false otherwise
     */
    public boolean isCreateBugButtonVisible() {
        return isVisible(createBugButton);
    }

    /**
     * Checks if the View Bugs button is visible.
     *
     * @return true if visible, false otherwise
     */
    public boolean isViewBugsButtonVisible() {
        return isVisible(viewBugsButton);
    }

    /**
     * Gets the text from the Create Bug button.
     *
     * @return button text
     */
    public String getCreateBugButtonText() {
        return text(createBugButton);
    }
}