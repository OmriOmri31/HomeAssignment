package framework.components;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Component for interacting with Android's native date picker dialogs.
 * Handles year selection, month navigation, and day selection.
 * Expects dates in dd/MM/yyyy format.
 */
public class AndroidDatePicker extends BasePage {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final By yearHeader;
    private final By yearList;
    private final By setButton;

    /**
     * Constructs an AndroidDatePicker with the given driver and timeout.
     *
     * @param driver the Android driver instance
     * @param explicitTimeout maximum wait time for picker interactions
     */
    public AndroidDatePicker(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);
        this.yearHeader = id("android:id/date_picker_header_year");
        this.yearList = id("android:id/date_picker_year_picker");
        this.setButton = id("android:id/button1");
    }


    /**
     * Opens a date picker field and selects the specified date.
     *
     * @param dateField the locator of the date input field to click
     * @param dateStr the date to select in dd/MM/yyyy format
     * @throws java.time.format.DateTimeParseException if date format is invalid
     * @throws IllegalStateException if date picker UI structure has changed
     */
    public void pick(By dateField, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, INPUT_FORMAT);
        click(dateField);
        waitVisible(yearHeader);

        selectYear(date.getYear());
        selectMonthAndDay(date.getMonthValue(), date.getDayOfMonth());

        click(setButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(yearHeader));
    }

    /**
     * Selects a year in the date picker by scrolling through the year list.
     * Intelligently scrolls based on distance from current year.
     *
     * @param year the four digit year to select
     * @throws IllegalStateException if year header is not numeric or year cannot be found
     */
    private void selectYear(int year) {
        click(yearHeader);
        waitVisible(yearList);

        String headerText = waitVisible(yearHeader).getText();
        int current;
        try {
            current = Integer.parseInt(headerText.replaceAll("\\D",""));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Year header not numeric: '" + headerText + "' (UI changed?)", e);
        }

        By yearItem = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"android:id/date_picker_year_picker\")" +
                        ".childSelector(new UiSelector().className(\"android.widget.TextView\").text(\"" + year + "\"))");

        String scroller =
                "new UiScrollable(new UiSelector().resourceId(\"android:id/date_picker_year_picker\")).setAsVerticalList()";

        int swipes = 0, maxSwipes = Math.abs(year - current) + 3;
        while (driver.findElements(yearItem).isEmpty() && swipes++ < maxSwipes) {
            if (year < current) {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + ".scrollBackward()"));
            } else {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + ".scrollForward()"));
            }
        }

        if (driver.findElements(yearItem).isEmpty()) {
            String toEnd = (year < current) ? ".flingToBeginning(10)" : ".flingToEnd(10)";
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + toEnd));
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                    scroller + ".scrollIntoView(new UiSelector().className(\"android.widget.TextView\").text(\"" + year + "\"))"
            ));
        }
        click(yearItem);
        wait.until(ExpectedConditions.textToBe(yearHeader, String.valueOf(year)));
    }

    /**
     * Navigates to the specified month and selects the day.
     *
     * @param month the month number (1-12)
     * @param day the day of month (1-31)
     */
    private void selectMonthAndDay(int month, int day) {
        goToMonth(month);
        clickDay(day);
    }

    /**
     * Navigates to the specified month by clicking next/previous month arrows.
     *
     * @param month the target month number (1-12)
     */
    private void goToMonth(int month) {
        while (true) {
            int currentMonth = getCurrentMonth();
            if (currentMonth == month) break;

            int before = currentMonth;
            click(month > currentMonth ? id("android:id/next") : id("android:id/prev"));
            wait.until(d -> getCurrentMonth() != before);
        }
    }

    /**
     * Determines the currently displayed month in the date picker.
     * Parses the month from the first day's content description.
     *
     * @return the current month number (1-12)
     * @throws IllegalStateException if month name cannot be parsed or device language is unsupported
     */
    private int getCurrentMonth() {
        WebElement firstOfMonth = driver.findElement(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.view.View\").descriptionStartsWith(\"01 \")"
                )
        );

        String desc = firstOfMonth.getAttribute("content-desc");
        String[] parts = desc == null ? new String[0] : desc.split(" ");
        if (parts.length < 3)
            throw new IllegalStateException("Unexpected date description: '" + desc + "' (UI changed?)");

        String monthName = parts[1];
        final Month monthEnum;
        try {
            monthEnum = Month.valueOf(monthName.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            throw new IllegalStateException("Unsupported month '" + monthName + "' in '" + desc + "'. Device language/UI likely changed.", e);
        }
        return monthEnum.getValue();
    }

    /**
     * Clicks on the specified day in the currently displayed month.
     *
     * @param day the day number to click (1-31)
     */
    private void clickDay(int day) {
        By dayLocator = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + day + "\")"
        );
        click(dayLocator);
    }
}
