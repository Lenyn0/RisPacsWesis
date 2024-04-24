package sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getResourceAsStream("/config/socket.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getSocketAddress() {
        return properties.getProperty("socket.address");
    }

    public static int getSocketPort() {
        return Integer.parseInt(properties.getProperty("socket.port"));
    }
}
