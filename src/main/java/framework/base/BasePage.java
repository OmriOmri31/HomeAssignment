package framework.base;

import framework.pages.CreateBugPage;
import framework.pages.ViewBugsPage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected final AndroidDriver driver;
    protected final Duration explicitTimeout;
    protected final WebDriverWait wait;

    protected final By createBugButton = textElement("Create Bug");
    protected final By viewBugsButton = textElement("View Bugs");
    protected final By homeButton = textElement("Home");

    public BasePage(AndroidDriver driver, Duration explicitTimeout){
        this.driver = driver;
        this.explicitTimeout = explicitTimeout;
        this.wait = new WebDriverWait(driver, explicitTimeout);
    }

    protected WebElement waitVisible (By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator){
        scrollIntoViewIfNeeded(locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator){
        WebElement element = waitClickable(locator);
        scrollIntoViewIfNeeded(locator);
        element.click();
    }

    protected void type(By locator, String text){
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String text(By locator){
        WebElement element = waitVisible(locator);
        return element.getText();
    }

    protected boolean isVisible(By locator){
        Duration shortWait = explicitTimeout.dividedBy(4);
        Duration minWait = Duration.ofSeconds(1);
        Duration maxWait = Duration.ofSeconds(5);
        if (shortWait.compareTo(minWait) < 0) shortWait = minWait;
        if (shortWait.compareTo(maxWait) > 0) shortWait = maxWait;

        WebDriverWait tempWaiter = new WebDriverWait(driver, shortWait);
        try{
            tempWaiter.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch(TimeoutException e){
            return false;
        }
    }

    protected By id(String resourceId){
        return AppiumBy.id(resourceId);
    }

    protected By resourceId(String resourceId){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"" + resourceId + "\")");
    }

    protected boolean assertOnPage(By screenRoot) {
        try {
            waitVisible(screenRoot);
        } catch (TimeoutException e){
            return false;
        }
        return true;
    }

    protected By textElement(String text){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + text + "\")");
    }

    protected String normalize(String s) {
        return s == null ? "" : s.trim();
    }

    public CreateBugPage clickCreateBug() {
        click(createBugButton);
        return new CreateBugPage(driver, explicitTimeout);
    }

    public ViewBugsPage clickViewBugs() {
        click(viewBugsButton);
        return new ViewBugsPage(driver, explicitTimeout);
    }

    public void clickHome() {
        click(homeButton);
    }

    public boolean isCreateBugButtonVisible() {
        return isVisible(createBugButton);
    }

    public boolean isViewBugsButtonVisible() {
        return isVisible(viewBugsButton);
    }

    public String getCreateBugButtonText() {
        return text(createBugButton);
    }

    public void scroll(String direction) {
        String normalized = normalize(direction).toLowerCase();
        if (!normalized.equals("up") && !normalized.equals("down")) {
            throw new IllegalArgumentException("Direction must be 'up' or 'down'");
        }

        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();
        int centerX = width / 2;

        int startY, endY;

        if (normalized.equals("down")) {
            startY = height * 3 / 4;
            endY = height / 2;
        } else {
            startY = height / 2;
            endY = height * 65 / 70;
        }

        performSwipe(centerX, startY, centerX, endY);
        performSwipe(centerX, startY, centerX, endY);
    }

    public void scrollIntoViewIfNeeded(By locator) {
        if (isVisible(locator)) return;
        for(int i = 0; i < 3; i++)
        {scroll("down"); if (isVisible(locator)) return;}
        for(int i = 0; i < 5; i++)
        {scroll("up"); if (isVisible(locator)) return;}
    }

    private void performSwipe(int startX, int startY, int endX, int endY) {
        org.openqa.selenium.interactions.PointerInput finger =
                new org.openqa.selenium.interactions.PointerInput(
                        org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
        org.openqa.selenium.interactions.Sequence swipe =
                new org.openqa.selenium.interactions.Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(900),
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(java.util.Collections.singletonList(swipe));
    }

}
