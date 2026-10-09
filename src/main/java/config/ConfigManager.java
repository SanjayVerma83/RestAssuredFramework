package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private Properties properties;

    public ConfigManager() {

        properties = new Properties();

        try {
            FileInputStream fileInputStream =
                    new FileInputStream("src/test/resources/config.properties");

            properties.load(fileInputStream);

            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
