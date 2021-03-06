package core.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String getProperty(String filePath,String propertyName) {
        return getProperties(filePath).getProperty(propertyName);
    }

    public static Properties getProperties(String filePath) {
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(filePath));
            properties.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
