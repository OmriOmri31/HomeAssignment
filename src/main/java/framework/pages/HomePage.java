package framework.pages;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.time.Duration;

public class HomePage extends BasePage {
    private final By screenRoot;

    /**
     * Constructs the HomePage with the given driver and timeout.
     *
     * @param driver the Android driver instance
     * @param explicitTimeout maximum wait time for page elements
     */
    public HomePage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);
        this.screenRoot = resourceId("homePage");
    }


    /**
     * Verifies that the home page is currently displayed.
     *
     * @return true if the home page is visible, false otherwise
     */
    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }
}
