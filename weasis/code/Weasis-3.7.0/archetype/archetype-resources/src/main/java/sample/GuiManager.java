package sample;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;

/**
 * GuiManager class is responsible for managing the GUI of the application.
 * It provides methods to display the results of anomaly detection and search in a GUI.
 */
public class GuiManager {
    public void addButton(SampleToolBar toolBar, String toolTip, String iconPath, ActionListener actionListener) {
        final JButton button = new JButton();
        button.setToolTipText(toolTip);
        button.setIcon(new ImageIcon(SampleToolBar.class.getResource(iconPath)));
        button.addActionListener(actionListener);
        toolBar.add(button);
    }

    public void ReportResultGUI(String jsonReport) {
        try {
            JFrame frame = new JFrame("报告生成结果");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 200);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(5, 5, 5, 5);

            addComponent(panel, "报告生成结果:", jsonReport, gbc, 0);
            frame.add(panel);
            frame.setLocationRelativeTo(null); // 居中显示
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void addComponent(JPanel panel, String labelName, String jsonReport, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelName);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // 使用 javax.json 进行解析
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonReport))) {
            JsonObject jsonObject = jsonReader.readObject();
            String content = jsonObject.getString(labelName.replace(":", ""));
            textArea.setText(content);
        }

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JScrollPane(textArea), gbc);
    }

    /**
     * Displays the result of anomaly detection in a GUI.
     *
     * @param jsonStr the JSON string containing the result of anomaly detection
     */
    public static void DetectionResultGUI(String jsonStr) {
        try {
            JFrame frame = new JFrame("异常检测结果");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 500);
            // 设置窗口最大化
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // 解析 JSON
            JsonReader reader = Json.createReader(new ByteArrayInputStream(jsonStr.getBytes()));
            JsonObject detectionResult = reader.readObject();
            reader.close();
            String checkResult = detectionResult.getString("异常分数");

            // 添加结果标签
            JLabel resultLabel = new JLabel("异常分数: " + checkResult);
            resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 20)); // 设置字体大小为 20，加粗
            frame.add(resultLabel, BorderLayout.NORTH);



            // 添加图像标签
            String imageData = detectionResult.getString("image_data");

            BufferedImage image =  decodeBase64ToImage(imageData);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            frame.add(imageLabel, BorderLayout.CENTER);
            frame.pack(); // 调整 JFrame 大小以适应其内容
            frame.setLocationRelativeTo(null); // 居中显示
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Decodes a Base64 encoded image data to a BufferedImage.
     *
     * @param imageData the Base64 encoded image data
     * @return the decoded BufferedImage
     * @throws IOException if an error occurs during decoding
     */
    private static BufferedImage decodeBase64ToImage(String imageData) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bis);
    }


    /**
     * Displays the result of search in a GUI.
     *
     * @param jsonStr the JSON string containing the result of search
     */
    public  void SearchResultGUI(String jsonStr) {
        try {
            JFrame frame = new JFrame("检索结果");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 500);
            // 设置窗口最大化
            //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            // 设置布局
            frame.setLayout(new BorderLayout());
            // 解析 JSON
            JsonReader reader = Json.createReader(new ByteArrayInputStream(jsonStr.getBytes()));
            JsonObject searchResult = reader.readObject();
            reader.close();

            // 创建缩略图面板
            JPanel thumbnailPanel = new JPanel(new FlowLayout());
            // 使用一个JScrollPane来放置缩略图面板
            JScrollPane scrollPane = new JScrollPane(thumbnailPanel);
            //frame.add(scrollPane, BorderLayout.NORTH);




            BufferedImage image = null;
            String report="";
            if (searchResult.size() > 0) {
                image = decodeBase64ToImage(searchResult.getString("image_data1"));
                report = searchResult.getString("report_data1");

            }
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            frame.add(imageLabel, BorderLayout.CENTER);

            if (searchResult.size() > 0) {
                // 设置scrollPane的宽度与imageLabel相同，高度自动调整
                scrollPane.setPreferredSize(new Dimension(imageLabel.getWidth(), 100));
            }
            frame.add(scrollPane, BorderLayout.NORTH);

            JTextArea reportTextArea = new JTextArea(report);
            Font font = new Font("宋体", Font.BOLD, 20); // 设置字体为宋体，加粗，大小为20
            reportTextArea.setFont(font);
            reportTextArea.setEditable(false);
            JScrollPane reportScrollPane = new JScrollPane(reportTextArea);
            frame.add(reportScrollPane, BorderLayout.SOUTH);



            // 添加点击事件监听器
            ActionListener thumbnailClickListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String imageData = button.getClientProperty("image_data").toString();
                    String reportData = button.getClientProperty("report_data").toString();
                    showFullImage(imageData, reportData, imageLabel, reportTextArea);
                }
            };

            // 遍历图像数据并创建缩略图按钮
            for (int i = 1; i <= searchResult.size()/2; i++) {
                String imageKey = "image_data" + i;
                String imageData = searchResult.getString(imageKey);

                String reportKey = "report_data" + i;
                String reportData = searchResult.getString(reportKey);

                image = decodeBase64ToImage(imageData);
                ImageIcon icon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));

                JButton thumbnailButton = new JButton(icon);
                thumbnailButton.putClientProperty("image_data", imageData);
                thumbnailButton.putClientProperty("report_data", reportData);
                thumbnailButton.addActionListener(thumbnailClickListener);
                thumbnailPanel.add(thumbnailButton);
            }

            frame.pack(); // 调整 JFrame 大小以适应其内容
            frame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the full image and report data in the GUI.
     *
     * @param imageData the Base64 encoded image data
     * @param reportData the report data
     * @param imageLabel the JLabel to display the image
     * @param reportTextArea the JTextArea to display the report data
     */
    private static void showFullImage(String imageData, String reportData,JLabel imageLabel,JTextArea reportTextArea) {
        try {
            BufferedImage image = decodeBase64ToImage(imageData);
            ImageIcon icon = new ImageIcon(image);
            imageLabel.setIcon(icon);
            reportTextArea.setText(reportData); // 更新报告文本区域
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
