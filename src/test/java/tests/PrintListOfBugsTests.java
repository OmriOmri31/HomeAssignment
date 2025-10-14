package tests;

import framework.driver.DriverFactory;
import framework.pages.HomePage;
import framework.pages.ViewBugsPage;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrintListOfBugsTests {

    private AndroidDriver driver;
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    @BeforeEach
    void setUp() {
        driver = DriverFactory.getDriver();
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test
    @DisplayName("Print all of the bug list")
    void printAllBugs() throws InterruptedException {
        HomePage home = new HomePage(driver, TIMEOUT);
        assertTrue(home.assertOnPage(), "Home page should be visible");
        ViewBugsPage viewBug = home.clickViewBugs();
        assertTrue(viewBug.assertOnPage(), "View Bug page should be visible");
        viewBug.clickButtonAll();
        int timesToScroll = 1; //Change depends on the magnitude of the list size
        for (int i = 0; i < timesToScroll; i++) {
            viewBug.scroll("down");
            Thread.sleep(500);
        }
        String[] bugList = viewBug.getBugList();
        for (String bug : bugList) {
            System.out.println(bug);
        }
    }

}
