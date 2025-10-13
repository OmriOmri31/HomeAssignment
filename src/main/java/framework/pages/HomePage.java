package framework.pages;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.time.Duration;

public class HomePage extends BasePage {
    private final By screenRoot;

    public HomePage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);
        this.screenRoot = resourceId("homePage");
    }

    public boolean verifyHomePage() {
        return assertOnPage(screenRoot);
    }
}
