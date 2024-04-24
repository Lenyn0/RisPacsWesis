package sample;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

/**
 * LoggerManager class is responsible for managing the logger instances.
 * It provides a method to get a logger for a specific class.
 */
public class LoggerManager {

    /**
     * Returns a logger for the given class.
     * The logger writes to a file named "application%g.log", where %g is a unique number.
     * Each log file is limited to 5MB, and a maximum of 3 log files are kept.
     *
     * @param clazz the class for which the logger is to be obtained
     * @return the logger for the given class
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());

        try {
            // 设置每个日志文件的大小为5MB，最多保留3个日志文件
            int fileSize = 5 * 1024 * 1024; // 5MB
            int fileCount = 3;
            FileHandler fileHandler = new FileHandler("application%g.log", fileSize, fileCount, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while setting up logger.", e);
        }

        return logger;
    }
}