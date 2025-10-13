package framework.pages;

import framework.base.BasePage;
import framework.components.AndroidDatePicker;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public class EditBugPage extends BasePage {
    private final By screenRoot;
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
    private final By saveChanges;
    private final By cancelEditing;
    private final AndroidDatePicker datePicker;

    public EditBugPage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);

        this.screenRoot = textElement("Edit Bug");
        this.bugTitle = resourceId("editBugTitle");
        this.bugSteps = resourceId("editBugSteps");
        this.bugExpectedResult = resourceId("editBugExpectedResult");
        this.bugActualResult = resourceId("editBugActualResult");
        this.bugStatus = resourceId("editBugStatus");
        this.bugSeverity = resourceId("editBugSeverity");
        this.bugPriority = resourceId("editBugPriority");
        this.bugDetectedBy = resourceId("editBugDetectedBy");
        this.bugFixedBy = resourceId("editBugFixedBy");
        this.bugDate = resourceId("editBugDate");
        this.bugDateClosed = resourceId("editBugDateClosed");
        this.bugFile = resourceId("editBugFile");
        this.saveChanges = textElement("Save Changes");
        this.cancelEditing = textElement("Cancel Editing");
        this.datePicker = new AndroidDatePicker(driver, explicitTimeout);
    }

    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }

    public EditBugPage pickDate(String date) {
        scrollIntoViewIfNeeded(bugDate);
        datePicker.pick(bugDate, date);
        return this;
    }

    public EditBugPage enterTitle(String value) {
        scrollIntoViewIfNeeded(bugTitle);
        type(bugTitle, normalize(value));
        return this;
    }

    public EditBugPage enterSteps(String value) {
        scrollIntoViewIfNeeded(bugSteps);
        type(bugSteps, normalize(value));
        return this;
    }

    public EditBugPage enterExpected(String value) {
        scrollIntoViewIfNeeded(bugExpectedResult);
        type(bugExpectedResult, normalize(value));
        return this;
    }

    public EditBugPage enterActual(String value) {
        scrollIntoViewIfNeeded(bugActualResult);
        type(bugActualResult, normalize(value));
        return this;
    }

    public EditBugPage setStatus(String value) {
        scrollIntoViewIfNeeded(bugStatus);
        return selectDropdownOption(bugStatus, normalize(value), "Status");
    }

    public EditBugPage setSeverity(String value) {
        scrollIntoViewIfNeeded(bugSeverity);
        return selectDropdownOption(bugSeverity, normalize(value), "Severity");
    }

    public EditBugPage setPriority(String value) {
        scrollIntoViewIfNeeded(bugPriority);
        return selectDropdownOption(bugPriority, normalize(value), "Priority");
    }

    public EditBugPage setDetectedBy(String value) {
        scrollIntoViewIfNeeded(bugDetectedBy);
        type(bugDetectedBy, normalize(value));
        return this;
    }

    public EditBugPage setFixedBy(String value) {
        scrollIntoViewIfNeeded(bugFixedBy);
        type(bugFixedBy, normalize(value));
        return this;
    }

    public EditBugPage pickDateClosed(String date) {
        scrollIntoViewIfNeeded(bugDateClosed);
        datePicker.pick(bugDateClosed, date);
        return this;
    }

    public void save() {
        scrollIntoViewIfNeeded(saveChanges);
        click(saveChanges);
    }

    public void cancel() {
        scrollIntoViewIfNeeded(cancelEditing);
        click(cancelEditing);
    }

    public String getTextByLocator(By locator) {
        return text(locator);
    }

    private EditBugPage selectDropdownOption(By field, String value, String fieldName) {
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
