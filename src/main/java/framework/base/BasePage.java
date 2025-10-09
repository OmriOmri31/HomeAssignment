//This class gives page objects safe actions that wait before interacting
package framework.base;

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
     * Creates an Android resource-id locator.
     * @param resourceId fully qualified resource id (e.g., com.app:id/title)
     * @return By locator for the resource id
     */
    protected By id(String resourceId){
        return AppiumBy.id(resourceId);
    }
}
