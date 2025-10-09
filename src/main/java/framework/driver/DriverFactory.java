package framework.driver;

import framework.utils.Config;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public final class DriverFactory {
    private static AndroidDriver driver;

    private DriverFactory() {}

    public static AndroidDriver getDriver() {
        if (driver == null) {
            driver = create();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static AndroidDriver create() {
        // Resolve APK to an absolute path so Appium doesn't depend on your working dir
        String appRelative = Config.get("app.path");
        String appAbsolute = Paths.get(appRelative).toAbsolutePath().toString();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(Config.get("platformName"))
                .setAutomationName(Config.get("automationName"))
                .setDeviceName(Config.get("deviceName"))
                .setPlatformVersion(Config.get("platformVersion"))
                .setApp(appAbsolute)
                // optional nice defaults:
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAutoGrantPermissions(true);

        try {
            AndroidDriver drv = new AndroidDriver(new URL(Config.get("server.url")), options);
            // small implicit to smooth out element presence
            drv.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(Config.getInt("implicitTimeoutSec")));
            return drv;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start AndroidDriver. Is Appium running? " +
                    "Server: " + Config.get("server.url") + ", app: " + appAbsolute, e);
        }
    }
}
