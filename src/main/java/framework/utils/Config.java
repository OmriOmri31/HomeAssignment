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

    /**
     * Retrieves a required configuration value by key.
     *
     * @param key the property key to look up
     * @return the trimmed property value
     * @throws NullPointerException if the key is not found in config.properties
     */
    public static String get(String key) {
        String value = PROPS.getProperty(key);
        return Objects.requireNonNull(value, "Missing config key: " + key).trim();
    }

    /**
     * Retrieves a configuration value as an integer.
     *
     * @param key the property key to look up
     * @return the property value parsed as an integer
     * @throws NullPointerException if the key is not found
     * @throws NumberFormatException if the value cannot be parsed as an integer
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Retrieves an optional configuration value by key.
     *
     * @param key the property key to look up
     * @return the trimmed property value, or null if the key doesn't exist
     */
    public static String getOrNull(String key) {
        String v = PROPS.getProperty(key);
        return v == null ? null : v.trim();
    }
}
