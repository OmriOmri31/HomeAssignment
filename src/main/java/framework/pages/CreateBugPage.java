package framework.pages;

import framework.base.BasePage;
import framework.components.AndroidDatePicker;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/** Page object for the "Create Bug" screen. */
public class CreateBugPage extends BasePage {

    private final By screenRoot;
    private final By bugId;
    private final By bugTitle;
    private final By bugSteps;
    private final By bugExpectedResult;
    private final By bugActualResult;
    private final By bugStatus;
    private final By bugSeverity;
    private final By bugPriority;
    private final By bugDetectedBy;
    private final By bugFixedBy;
    private final By bugDate;
    private final By bugDateClosed;
    private final By bugFile;
    private final By addBugButton;
    private final AndroidDatePicker datePicker;

    /**
     * Constructs the CreateBugPage.
     * @param driver Android driver for interactions (non-null)
     * @param explicitTimeout max wait per explicit condition
     */
    public CreateBugPage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);

        this.screenRoot = textElement("Create a Bug");
        this.bugId = resourceId("bugId");
        this.bugTitle = resourceId("bugTitle");
        this.bugSteps = resourceId("bugSteps");
        this.bugExpectedResult = resourceId("bugExpectedResult");
        this.bugActualResult = resourceId("bugActualResult");
        this.bugStatus = resourceId("bugStatus");
        this.bugSeverity = resourceId("bugSeverity");
        this.bugPriority = resourceId("bugPriority");
        this.bugDetectedBy = resourceId("bugDetectedBy");
        this.bugFixedBy = resourceId("bugFixedBy");
        this.bugDate = resourceId("bugDate");
        this.bugDateClosed = resourceId("bugDateClosed");
        this.bugFile = resourceId("bugFile");
        this.addBugButton = textElement("Add Bug");
        this.datePicker = new AndroidDatePicker(driver, explicitTimeout);
    }

    /**
     * Verifies that the Create Bug page is displayed.
     * @return true if page is visible, false otherwise
     */
    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }

    /**
     * Enters the bug ID using setValue for numeric keyboard compatibility.
     * @param value bug ID value
     * @return this CreateBugPage instance
     */
    public CreateBugPage enterBugId(String value) {
        String normalized = normalize(value);

        // Wait for element
        WebElement element = waitVisible(bugId);

        // Click to focus
        element.click();

        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Clear first
        element.clear();

        // Use Appium's setValue through executeScript
        driver.executeScript("mobile: type",
                java.util.Map.of("elementId", ((org.openqa.selenium.remote.RemoteWebElement) element).getId(),
                        "text", normalized));

        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Hide keyboard
        try {
            driver.hideKeyboard();
        } catch (Exception e) {
            // Keyboard might already be hidden
        }

        return this;
    }

    /**
     * Selects a date using the date picker.
     * @param date date in dd/MM/yyyy format
     * @return this CreateBugPage instance
     */
    public CreateBugPage pickDate(String date) {
        datePicker.pick(bugDate, date);
        return this;
    }

    /**
     * Enters the bug title.
     * @param value bug title
     * @return this CreateBugPage instance
     */
    public CreateBugPage enterTitle(String value) {
        type(bugTitle, normalize(value));
        return this;
    }

    /**
     * Enters the steps to recreate the bug.
     * @param value steps description
     * @return this CreateBugPage instance
     */
    public CreateBugPage enterSteps(String value) {
        type(bugSteps, normalize(value));
        return this;
    }

    /**
     * Enters the expected result.
     * @param value expected result description
     * @return this CreateBugPage instance
     */
    public CreateBugPage enterExpected(String value) {
        type(bugExpectedResult, normalize(value));
        return this;
    }

    /**
     * Enters the actual result.
     * @param value actual result description
     * @return this CreateBugPage instance
     */
    public CreateBugPage enterActual(String value) {
        type(bugActualResult, normalize(value));
        return this;
    }

    /**
     * Sets the bug status from dropdown.
     * @param value status option (e.g., "Open", "Fixed", "Closed")
     * @return this CreateBugPage instance
     */
    public CreateBugPage setStatus(String value) {
        return selectDropdownOption(bugStatus, normalize(value), "Status");
    }

    /**
     * Sets the bug severity from dropdown.
     * @param value severity option (e.g., "Critical", "Major", "Minor")
     * @return this CreateBugPage instance
     */
    public CreateBugPage setSeverity(String value) {
        return selectDropdownOption(bugSeverity, normalize(value), "Severity");
    }

    /**
     * Sets the bug priority from dropdown.
     * @param value priority option (e.g., "High", "Medium", "Low")
     * @return this CreateBugPage instance
     */
    public CreateBugPage setPriority(String value) {
        return selectDropdownOption(bugPriority, normalize(value), "Priority");
    }

    /**
     * Enters the name of the person who detected the bug.
     * @param value detector name
     * @return this CreateBugPage instance
     */
    public CreateBugPage setDetectedBy(String value) {
        type(bugDetectedBy, normalize(value));
        return this;
    }

    /**
     * Enters the name of the person who fixed the bug.
     * @param value fixer name
     * @return this CreateBugPage instance
     */
    public CreateBugPage setFixedBy(String value) {
        type(bugFixedBy, normalize(value));
        return this;
    }

    /**
     * Selects the date the bug was closed.
     * @param date date in dd/MM/yyyy format
     * @return this CreateBugPage instance
     */
    public CreateBugPage pickDateClosed(String date) {
        datePicker.pick(bugDateClosed, date);
        return this;
    }

    /**
     * Submits the bug creation form.
     */
    public void submit() {
        click(addBugButton);
    }

    /**
     * Selects an option from a dropdown/spinner field.
     * @param field the dropdown field locator
     * @param value the option text to select
     * @param fieldName field name for error messages
     * @return this CreateBugPage instance
     */
    private CreateBugPage selectDropdownOption(By field, String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " value must not be blank");
        }

        click(field);

        By option = textElement(value);
        if (!isVisible(option)) {
            try {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                                ".scrollIntoView(new UiSelector().text(\"" + value + "\"))"));
            } catch (NoSuchElementException e) {
                throw new AssertionError(fieldName + " option not found: '" + value + "'", e);
            }
        }
        click(option);
        return this;
    }
}