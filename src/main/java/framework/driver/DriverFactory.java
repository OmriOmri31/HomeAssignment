package framework.driver;

import framework.utils.Config;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public final class DriverFactory {
    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {}

    public static AndroidDriver getDriver() {
        AndroidDriver d = DRIVER.get();
        if (d == null) {
            d = create();
            DRIVER.set(d);
        }
        return d;
    }

    public static void quitDriver() {
        AndroidDriver d = DRIVER.get();
        if (d != null) {
            d.quit();
            DRIVER.remove();
        }
    }

    private static AndroidDriver create() {
        String appRelative = Config.get("app.path");
        String appAbsolute = Paths.get(appRelative).toAbsolutePath().toString();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(Config.get("platformName"))
                .setAutomationName(Config.get("automationName"))
                .setDeviceName(Config.get("deviceName"))
                .setPlatformVersion(Config.get("platformVersion"))
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAutoGrantPermissions(true);

        String appPackage = Config.getOrNull("app.package");
        String appActivity = Config.getOrNull("app.activity");
        String noReset = Config.getOrNull("noReset");
        if (noReset != null && noReset.equalsIgnoreCase("true")) {
            options.setNoReset(true);
        }
        if (appPackage != null && appActivity != null) {
            options.setAppPackage(appPackage).setAppActivity(appActivity);
        } else {
            options.setApp(appAbsolute);
        }

        try {
            AndroidDriver drv = new AndroidDriver(new URL(Config.get("server.url")), options);
            drv.manage().timeouts().implicitlyWait(Duration.ZERO);
            return drv;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start AndroidDriver. Is Appium running? " +
                    "Server: " + Config.get("server.url") + ", app: " + appAbsolute, e);
        }
    }
}
