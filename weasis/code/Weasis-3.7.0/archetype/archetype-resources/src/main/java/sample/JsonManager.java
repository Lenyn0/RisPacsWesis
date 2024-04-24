package sample;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JsonManager class is responsible for managing the JSON messages received from the server.
 * It provides a method to receive and print the message.
 */
public class JsonManager {

    private static final Logger logger = LoggerManager.getLogger(JsonManager.class);
    private final GuiManager guiManager;

    /**
     * Constructor for JsonManager.
     * Initializes the GuiManager.
     */
    public JsonManager() {
        this.guiManager = new GuiManager();
    }

    /**
     * Receives a message from the server and prints it.
     * If the flag is "reportGeneration", "anomalyDetection", or "search", it displays the result in a GUI.
     *
     * @param socket the socket connected to the server
     * @param flag the flag indicating the type of the message
     */
    public void receiveAndPrintMessage(Socket socket, String flag) {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));  // 指定 UTF-8 编码

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonReport = jsonBuilder.toString();
            logger.info("Medical report received: " + jsonReport);
            if(flag.equals("reportGeneration")){
                SwingUtilities.invokeLater(() -> guiManager.ReportResultGUI(jsonReport));
            } else if (flag.equals("anomalyDetection")) {
                SwingUtilities.invokeLater(() -> guiManager.DetectionResultGUI(jsonReport));
            }
            else if (flag.equals("search")) {
                SwingUtilities.invokeLater(() -> guiManager.SearchResultGUI(jsonReport));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while receiving message.", e);
        }
    }
}