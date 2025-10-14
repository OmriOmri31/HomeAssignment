package tests.base;

import framework.driver.DriverFactory;
import framework.pages.CreateBugPage;
import framework.pages.EditBugPage;
import framework.pages.HomePage;
import framework.pages.ViewBugsPage;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Base class for all test classes providing common setup, teardown, and utility methods.
 * Manages driver lifecycle and page object initialization.
 * All test classes should extend this class.
 */
public abstract class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected AndroidDriver driver;
    protected static final Duration TIMEOUT = Duration.ofSeconds(15);
    protected static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private HomePage homePage;
    private CreateBugPage createBugPage;
    private ViewBugsPage viewBugsPage;
    private EditBugPage editBugPage;

    /**
     * Initializes the driver and ensures the app starts on the home page before each test.
     */
    @BeforeEach
    void setUp() {
        logger.info("=== Starting Test ===");
        driver = DriverFactory.getDriver();
        resetPageObjects();
        ensureHomePage();
    }

    /**
     * Quits the driver and resets page objects after each test.
     */
    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
        resetPageObjects();
        logger.info("=== Test Completed ===");
    }

    /**
     * Ensures the app is on the home page by detecting current page and navigating if needed.
     * Attempts to identify the current page and click Home, or scrolls up if unrecognized.
     */
    protected void ensureHomePage() {
        Duration shortTimeout = Duration.ofSeconds(1);
        HomePage home = new HomePage(driver, shortTimeout);
        CreateBugPage create = new CreateBugPage(driver, shortTimeout);
        ViewBugsPage view = new ViewBugsPage(driver, shortTimeout);
        EditBugPage edit = new EditBugPage(driver, shortTimeout);

        if (home.assertOnPage()) {
            return;
        } else if (create.assertOnPage()) {
            create.clickHome();
        } else if (view.assertOnPage()) {
            view.clickHome();
        } else if (edit.assertOnPage()) {
            edit.clickHome();
        } else {
            home.scroll("up");
            home.scroll("up");
            home.scroll("up");
        }

        waitFor(500);
    }

    /**
     * Navigates to the home page and returns the HomePage instance
     *
     * @return the HomePage instance
     */
    protected HomePage navigateToHomePage() {
        ensureHomePage();
        return getHomePage();
    }

    /**
     * Gets or creates the HomePage instance
     *
     * @return the HomePage instance
     */
    protected HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage(driver, TIMEOUT);
        }
        return homePage;
    }

    /**
     * Gets or creates the CreateBugPage instance
     *
     * @return the CreateBugPage instance
     */
    protected CreateBugPage getCreateBugPage() {
        if (createBugPage == null) {
            createBugPage = new CreateBugPage(driver, TIMEOUT);
        }
        return createBugPage;
    }

    /**
     * Gets or creates the ViewBugsPage instance
     *
     * @return the ViewBugsPage instance
     */
    protected ViewBugsPage getViewBugsPage() {
        if (viewBugsPage == null) {
            viewBugsPage = new ViewBugsPage(driver, TIMEOUT);
        }
        return viewBugsPage;
    }

    /**
     * Gets or creates the EditBugPage instance
     *
     * @return the EditBugPage instance
     */
    protected EditBugPage getEditBugPage() {
        if (editBugPage == null) {
            editBugPage = new EditBugPage(driver, TIMEOUT);
        }
        return editBugPage;
    }

    /**
     * Resets all page object instances to null, forcing recreation on next access
     */
    private void resetPageObjects() {
        homePage = null;
        createBugPage = null;
        viewBugsPage = null;
        editBugPage = null;
    }

    /**
     * Generates  a unique bug ID based on current timestamp in milliseconds
     * which makes the test reusable
     * @return a unique bug ID string
     */
    protected String generateUniqueBugId() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Returns today's date in dd/MM/yyyy format.
     *
     * @return today's date as a formatted string
     */
    protected String today() {
        return LocalDate.now().format(DMY);
    }

    /**
     * Pauses execution for the specified duration.
     * Used  for timing sensitive operations where explicit waits aren't suitable
     *
     * @param milliseconds the duration to wait in milliseconds
     */
    protected void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}