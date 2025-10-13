package framework.utils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class Config {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("config.properties not found on classpath");
            PROPS.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private Config() {}

    public static String get(String key) {
        String value = PROPS.getProperty(key);
        return Objects.requireNonNull(value, "Missing config key: " + key).trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
