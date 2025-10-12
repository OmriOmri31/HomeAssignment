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

    /**
     * Constructs the HomePage.
     *
     * @param driver          Android driver for interactions (non-null)
     * @param explicitTimeout max wait per explicit condition
     */
    public HomePage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);
        this.screenRoot = resourceId("homePage");
    }
    /**
     * Verifies that the home page is displayed.
     *
     * @return true if home page is visible, false otherwise
     */
    public boolean verifyHomePage() {
        return assertOnPage(screenRoot);
    }

}