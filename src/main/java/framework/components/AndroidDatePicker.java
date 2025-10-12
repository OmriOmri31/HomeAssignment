package framework.components;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Component for interacting with Android's native DatePicker.
 */
public class AndroidDatePicker extends BasePage {

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final By yearHeader;
    private final By setButton;

    public AndroidDatePicker(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);
        this.yearHeader = id("android:id/date_picker_header_year");
        this.setButton = id("android:id/button1");
    }

    /**
     * Picks a date using the native Android date picker.
     * @param dateField the field that opens the date picker when clicked
     * @param dateStr date in dd/MM/yyyy format
     */
    public void pick(By dateField, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, INPUT_FORMAT);

        click(dateField);
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        selectYear(date.getYear());
        selectMonthAndDay(date.getMonthValue(), date.getDayOfMonth());

        click(setButton);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private void selectYear(int year) {
        // Open year list
        click(yearHeader);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        int current = Integer.parseInt(waitVisible(yearHeader).getText());

        By yearItem = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"android:id/date_picker_year_picker\")" +
                        ".childSelector(new UiSelector().className(\"android.widget.TextView\").text(\"" + year + "\"))"
        );

        String scroller =
                "new UiScrollable(new UiSelector().resourceId(\"android:id/date_picker_year_picker\")).setAsVerticalList()";

        // Scroll the correct way until the target year is visible (cap swipes to avoid infinite loops)
        int swipes = 0, maxSwipes = 30;
        while (driver.findElements(yearItem).isEmpty() && swipes++ < maxSwipes) {
            if (year < current) {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + ".scrollBackward()"));
            } else {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + ".scrollForward()"));
            }
        }

        // If still not visible, do a final scrollIntoView from the nearest end
        if (driver.findElements(yearItem).isEmpty()) {
            String toEnd = (year < current) ? ".flingToBeginning(10)" : ".flingToEnd(10)";
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + toEnd));
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                    scroller + ".scrollIntoView(new UiSelector().className(\"android.widget.TextView\").text(\"" + year + "\"))"
            ));
        }

        click(yearItem);
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }


    /**
     * Selects month and day from the calendar view.
     * @param month target month (1-12)
     * @param day target day
     */
    private void selectMonthAndDay(int month, int day) {
        goToMonth(month);
        clickDay(day);
    }

    /**
     * Navigates to the target month.
     * @param month target month (1-12)
     */
    private void goToMonth(int month) {
        while (true) {
            int currentMonth = getCurrentMonth();
            if (currentMonth == month) {
                break;
            }

            if (month > currentMonth) {
                click(id("android:id/next"));
            } else {
                click(id("android:id/prev"));
            }

            try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    /**
     * Gets the currently displayed month from the header.
     * @return month number (1-12)
     */
    private int getCurrentMonth() {
        WebElement firstOfMonth = driver.findElement(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.view.View\").descriptionStartsWith(\"01 \")"
                )
        );

        String desc = firstOfMonth.getAttribute("content-desc"); // e.g. "01 October 2025"
        String monthName = desc.split(" ")[1];                   // "October"
        return Month.valueOf(monthName.toUpperCase(Locale.ENGLISH)).getValue(); // 1..12
    }

    /**
     * Clicks a specific day in the calendar.
     * @param day day to click
     */
    private void clickDay(int day) {
        By dayLocator = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + day + "\")"
        );

        click(dayLocator);
        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}