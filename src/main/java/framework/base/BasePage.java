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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base class for all page objects, providing common mobile interaction methods.
 * Handles waiting, scrolling, element location, and navigation between pages.
 * All page objects should extend this class to inherit core functionality.
 */

public abstract class BasePage {
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected final AndroidDriver driver;
    protected final Duration explicitTimeout;
    protected final WebDriverWait wait;

    protected final By createBugButton = textElement("Create Bug");
    protected final By viewBugsButton = textElement("View Bugs");
    protected final By homeButton = textElement("Home");

    /**
     * Constructs a BasePage with the given driver and timeout settings.
     *
     * @param driver the Android driver instance for interactions
     * @param explicitTimeout maximum wait duration for explicit waits
     */
    public BasePage(AndroidDriver driver, Duration explicitTimeout){
        this.driver = driver;
        this.explicitTimeout = explicitTimeout;
        this.wait = new WebDriverWait(driver, explicitTimeout);
    }

    /**
     * Waits for an element to be visible on the screen.
     *
     * @param locator the element locator strategy
     * @return the visible WebElement
     * @throws TimeoutException if element is not visible within the timeout period
     */
    protected WebElement waitVisible (By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    /**
     * Waits for an element to be clickable, scrolling into view if needed.
     *
     * @param locator the element locator strategy
     * @return the clickable WebElement
     * @throws TimeoutException if element is not clickable within the timeout period
     */
    protected WebElement waitClickable(By locator){
        scrollIntoViewIfNeeded(locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Clicks an element after ensuring it's visible and clickable.
     * Scrolls the element into view if necessary before clicking.
     *
     * @param locator the element locator strategy
     * @throws TimeoutException if element cannot be clicked within the timeout period
     */
    public void click(By locator){
        WebElement element = waitClickable(locator);
        scrollIntoViewIfNeeded(locator);
        element.click();
    }

    /**
     * Types text into an input field after clearing existing content.
     *
     * @param locator the element locator strategy
     * @param text the text to enter
     * @throws TimeoutException if element is not visible within the timeout period
     */
    protected void type(By locator, String text){
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Retrieves the text content of an element.
     *
     * @param locator the element locator strategy
     * @return the element's text content
     * @throws TimeoutException if element is not visible within the timeout period
     */
    protected String text(By locator){
        WebElement element = waitVisible(locator);
        return element.getText();
    }

    /**
     * Checks if an element is visible with a reduced timeout (1-5 seconds).
     * Non-blocking - returns false quickly if element is not found.
     *
     * @param locator the element locator strategy
     * @return true if element is visible, false otherwise
     */
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

    /**
     * Creates a locator for an element by its Android resource ID.
     *
     * @param resourceId the resource ID (e.g., "com.example:id/button")
     * @return a By locator for the resource ID
     */
    protected By id(String resourceId){
        return AppiumBy.id(resourceId);
    }

    /**
     * Creates a UiAutomator locator for an element by resource ID.
     *
     * @param resourceId the resource ID to locate
     * @return a By locator using UiAutomator strategy
     */
    protected By resourceId(String resourceId){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"" + resourceId + "\")");
    }

    /**
     * Verifies that the current page is displayed by checking for its root element.
     *
     * @param screenRoot the locator for the page's root identifying element
     * @return true if the page is visible, false otherwise
     */
    protected boolean assertOnPage(By screenRoot) {
        try {
            waitVisible(screenRoot);
        } catch (TimeoutException e){
            return false;
        }
        return true;
    }

    /**
     * Creates a locator for an element by its exact text content.
     *
     * @param text the exact text to match
     * @return a By locator using UiAutomator text matching
     */
    protected By textElement(String text){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + text + "\")");
    }

    /**
     * Normalizes a string by trimming whitespace, converting null to empty string.
     *
     * @param s the string to normalize
     * @return the trimmed string, or empty string if input was null
     */
    protected String normalize(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Navigates to the Create Bug page by clicking the Create Bug button.
     *
     * @return a new CreateBugPage instance
     */
    public CreateBugPage clickCreateBug() {
        click(createBugButton);
        return new CreateBugPage(driver, explicitTimeout);
    }

    /**
     * Navigates to the View Bugs page by clicking the View Bugs button.
     *
     * @return a new ViewBugsPage instance
     */
    public ViewBugsPage clickViewBugs() {
        click(viewBugsButton);
        return new ViewBugsPage(driver, explicitTimeout);
    }

    /**
     * Navigates back to the home page by clicking the Home button.
     */
    public void clickHome() {
        click(homeButton);
    }

    /*public boolean isCreateBugButtonVisible() {
        return isVisible(createBugButton);
    }

    public boolean isViewBugsButtonVisible() {
        return isVisible(viewBugsButton);
    }

    public String getCreateBugButtonText() {
        return text(createBugButton);
    }*/

    /**
     * Performs a vertical scroll gesture in the specified direction.
     * Executes two consecutive swipes for more reliable scrolling.
     *
     * @param direction "up" or "down" (case insensitive)
     * @throws IllegalArgumentException if direction is not "up" or "down"
     */
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


    /**
     * Scrolls an element into view if it's not already visible.
     * Attempts scrolling down (3 times) then up (5 times) to find the element.
     *
     * @param locator the element to scroll into view
     * @throws AssertionError if element is not found after all scroll attempts
     */
    public void scrollIntoViewIfNeeded(By locator) {
        if (isVisible(locator)) return;

        logger.debug("Scrolling to find element: {}", locator);

        for(int i = 0; i < 3; i++)
        {scroll("down"); if (isVisible(locator)) return;}
        for(int i = 0; i < 5; i++)
        {scroll("up"); if (isVisible(locator)) return;}

        if (!isVisible(locator)) {
            logger.error("Element not found after scrolling: {}", locator);
            throw new AssertionError("Element not found after scrolling: " + locator);
        }
    }

    /**
     * Executes a swipe gesture using W3C Actions API.
     *
     * @param startX starting X coordinate
     * @param startY starting Y coordinate
     * @param endX ending X coordinate
     * @param endY ending Y coordinate
     */
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
