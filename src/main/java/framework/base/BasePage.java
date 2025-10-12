//This class gives page objects safe actions that wait before interacting
package framework.base;

import framework.pages.CreateBugPage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.TimeoutException;

/** Base class for screen objects; provides safe waits and interactions. */
public abstract class BasePage {
    protected final AndroidDriver driver;
    protected final Duration explicitTimeout;
    protected final WebDriverWait wait;

    /**
     * Constructs a page object.
     * @param driver Android driver for interactions (non-null)
     * @param explicitTimeout max wait per explicit condition
     */
    public BasePage(AndroidDriver driver, Duration explicitTimeout){
        this.driver = driver;
        this.explicitTimeout = explicitTimeout;
        this.wait = new WebDriverWait(driver, explicitTimeout);
    }

    /**
     * Waits until the element located by the locator is visible.
     * @param locator By locator of the target element
     * @return visible WebElement
     * @throws org.openqa.selenium.TimeoutException if not visible within timeout
     */
    protected WebElement waitVisible (By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element located by the locator is clickable.
     * @param locator By locator of the target element
     * @return clickable WebElement
     * @throws org.openqa.selenium.TimeoutException if not clickable within timeout
     */
    protected WebElement waitClickable(By locator){
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Clicks the element after ensuring it is clickable.
     * @param locator By locator of the target element
     */
    protected void click(By locator){
        WebElement element = waitClickable(locator);
        element.click();
    }

    /**
     * Clears existing text and types the given value after ensuring visibility.
     * @param locator By locator of the input field
     * @param text text to enter (null treated as empty if applicable)
     */
    protected void type(By locator, String text){
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Returns the element's text after ensuring visibility.
     * @param locator By locator of the target element
     * @return non-null text (may be empty)
     */
    protected String text(By locator){
        WebElement element = waitVisible(locator);
        return element.getText();
    }

    /**
     * Quickly checks if the element becomes visible using a short timeout.
     * @param locator By locator of the target element
     * @return true if visible within short timeout; false otherwise
     */
    protected boolean isVisible(By locator){
        Duration shortWait = explicitTimeout.dividedBy(4);
        Duration minWait = Duration.ofSeconds(1);
        Duration maxWait = Duration.ofSeconds(5);
        if (shortWait.compareTo(minWait) < 0)
            shortWait = minWait;
        if (shortWait.compareTo(maxWait) > 0)
            shortWait = maxWait;
        WebDriverWait tempWaiter = new WebDriverWait(driver, shortWait);

        try{
            tempWaiter.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        }
        catch(TimeoutException e){
            return false;
        }
    }

    /**
     * Creates an Android resource-id locator for NATIVE elements.
     * Note: This does NOT work for elements inside WebViews.
     * For WebView elements, use resourceId() instead.
     *
     * @param resourceId fully qualified resource id (e.g., com.app:id/title)
     * @return By locator for the resource id
     */
    protected By id(String resourceId){
        return AppiumBy.id(resourceId);
    }

    /**
     * Creates a UiAutomator resource-id locator that works for both native
     * elements AND elements inside WebViews.
     *
     * @param resourceId simple resource id (e.g., "homePage" or "bugId")
     * @return By locator using UiAutomator
     */
    protected By resourceId(String resourceId){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"" + resourceId + "\")");
    }

    protected boolean assertOnPage(By screenRoot) {
        try {
            waitVisible(screenRoot);
        }
        catch (TimeoutException e){
                return false;
            }
            return true;

        }
    /**
     * Creates a UiAutomator locator for TextView elements by text.
     *
     * @param text exact text of the TextView element
     * @return By locator for the TextView
     */
    protected By textView(String text){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"android.widget.TextView\").text(\"" + text + "\")");
    }

    /**
     * Creates a UiAutomator locator for Button elements by text.
     *
     * @param text exact text of the Button element
     * @return By locator for the Button
     */
    protected By button(String text){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"android.widget.Button\").text(\"" + text + "\")");
    }

    /**
     * Creates a UiAutomator locator for any element by text (class-agnostic).
     *
     * @param text exact text of the element
     * @return By locator for any element with the given text
     */
    protected By textElement(String text){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + text + "\")");
    }

    /**
     * Creates a UiAutomator locator for any element by className and text.
     *
     * @param className full class name (e.g., "android.widget.TextView")
     * @param text exact text of the element
     * @return By locator for the element
     */
    protected By elementWithText(String className, String text){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"" + className + "\").text(\"" + text + "\")");
    }
    }

