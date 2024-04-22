package sample; /*******************************************************************************
 * Copyright (c) 2009-2020 Weasis Team and other contributors.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

//#set( $symbol_pound = '#' )
//#set( $symbol_dollar = '$' )
//#set( $symbol_escape = '\' )
//package ${package};

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.weasis.core.api.gui.Image2DViewer;
import org.weasis.core.api.gui.util.JMVUtils;
import org.weasis.core.api.image.SimpleOpManager;
import org.weasis.core.api.image.ZoomOp;
import org.weasis.core.api.media.data.ImageElement;
import org.weasis.core.ui.editor.SeriesViewerFactory;
import org.weasis.core.ui.editor.image.ImageViewerPlugin;
import org.weasis.core.ui.editor.image.ViewCanvas;
import org.weasis.core.ui.util.WtoolBar;
import org.weasis.dicom.codec.DicomImageElement;
import org.weasis.dicom.explorer.CheckTreeModel;
import org.weasis.dicom.explorer.DicomModel;
import org.weasis.dicom.explorer.ExportTree;
//import org.weasis.dicom.explorer.CheckTreeModel;
//import org.weasis.dicom.explorer.DicomModel;
//import org.weasis.dicom.explorer.ExportTree;
import org.weasis.dicom.viewer2d.EventManager;
import org.weasis.dicom.viewer2d.View2dContainer;
import org.weasis.opencv.op.ImageConversion;
import sample.MyImagePlugin;
import org.weasis.opencv.data.PlanarImage;


import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.net.Socket;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import javax.imageio.ImageIO;

import java.io.*;
import java.net.Socket;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Base64;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import java.nio.charset.StandardCharsets;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.List;

import org.weasis.opencv.data.PlanarImage;


public class SampleToolBar<E extends ImageElement> extends WtoolBar {
    //private final Hashtable<String, Object> properties;
    protected SampleToolBar(DicomModel model) {
        super("Sample Toolbar", 500);
        //this.properties = properties;
        final JButton reportGenerationButton = new JButton();
        reportGenerationButton.setToolTipText("报告生成");
        reportGenerationButton.setIcon(new ImageIcon(SampleToolBar.class.getResource("/icon/32x32/报告生成.png")));
        reportGenerationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof Component) {
                    Socket socket = null;
                    try {
                        socket = new Socket("localhost", 5555);
                        BufferedImage image = getImage();
                        if(image!=null){
                            sendImage(socket,image,"reportGeneration");
                            receiveAndPrintMessage(socket,"reportGeneration");
                        }
                        //socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        add(reportGenerationButton);

        final JButton anomalyDetectionButton = new JButton();
        anomalyDetectionButton.setToolTipText("异常检测");
        anomalyDetectionButton.setIcon(new ImageIcon(SampleToolBar.class.getResource("/icon/32x32/异常检测.png")));
        anomalyDetectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof Component) {
                    Socket socket = null;
                    try {
                        socket = new Socket("localhost", 5555);
                        BufferedImage image = getImage();
                        if(image!=null){
                            sendImage(socket,image,"anomalyDetection");
                            receiveAndPrintMessage(socket,"anomalyDetection");
                        }
                        //socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        add(anomalyDetectionButton);

        final JButton searchButton = new JButton();
        searchButton.setToolTipText("检索");
        searchButton.setIcon(new ImageIcon(SampleToolBar.class.getResource("/icon/32x32/检索.png")));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof Component) {
                    Socket socket = null;
                    try {
                        socket = new Socket("localhost", 5555);
                        BufferedImage image = getImage();
                        if(image!=null){
                            sendImage(socket,image,"search");
                            receiveAndPrintMessage(socket,"search");
                        }
                        //socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        add(searchButton);
    }
    // 将 JComponent 转换为 BufferedImage
    private static BufferedImage componentToImage(Component component) {
        // 创建与组件大小相同的 BufferedImage
        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // 创建图形上下文
        Graphics2D g = image.createGraphics();

        // 绘制组件到图像
        component.paint(g);

        // 释放图形上下文资源
        g.dispose();

        return image;
    }
    private static void sendImage(Socket socket,BufferedImage image,String flag) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            jsonObjectBuilder.add("flag", flag);
            jsonObjectBuilder.add("image", Base64.getEncoder().encodeToString(imageBytes));
            JsonObject jsonObject = jsonObjectBuilder.build();

            // 使用 OutputStreamWriter 和 BufferedWriter 发送 UTF-8 编码的数据
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(jsonObject.toString());
            writer.flush();

            // 关闭输出流
            socket.shutdownOutput();

            System.out.println("JSON with image sent to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveAndPrintMessage(Socket socket,String flag) {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));  // 指定 UTF-8 编码

            // 接收服务端返回的消息
/*            String message = reader.readLine();
            System.out.println("Server response: " + message);*/

            // 接收服务端返回的 JSON 数据
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonReport = jsonBuilder.toString();
            System.out.println("Medical report received: " + jsonReport);
            if(flag=="reportGeneration"){
                SwingUtilities.invokeLater(() -> createAndShowGUI(jsonReport));
            } else if (flag=="anomalyDetection") {
                SwingUtilities.invokeLater(() -> DetectionResultGUI(jsonReport));
            }
            else if (flag=="search") {
                SwingUtilities.invokeLater(() -> SearchResultGUI(jsonReport));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void SearchResultGUI(String jsonStr) {
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

    // 显示完整图像
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

    // 解码 base64 图像数据为 BufferedImage
    private static BufferedImage decodeBase64ToImage(String imageData) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bis);
    }
    private static void DetectionResultGUI(String jsonStr) {
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
    private static void createAndShowGUI(String jsonReport) {
        try {
            JFrame frame = new JFrame("报告生成结果");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 200);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(5, 5, 5, 5);

//            addComponent(panel, "临床症状:", jsonReport, gbc, 0);
//            addComponent(panel, "影像学表现:", jsonReport, gbc, 1);
//            addComponent(panel, "结论:", jsonReport, gbc, 2);
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


    public static BufferedImage getImage(){
        EventManager eventManager = EventManager.getInstance();
        ImageViewerPlugin<DicomImageElement> container = eventManager.getSelectedView2dContainer();
        List<ViewCanvas<DicomImageElement>> views = container.getImagePanels();
        if (views.get(0) instanceof Image2DViewer) {
            Image2DViewer<?> view2DPane = (Image2DViewer<?>) views.get(0);
            PlanarImage src = view2DPane.getSourceImage();
            if (src != null) {
                SimpleOpManager opManager = view2DPane.getImageLayer().getDisplayOpManager().copy();
                opManager.removeImageOperationAction(opManager.getNode(ZoomOp.OP_NAME));
                opManager.setFirstNode(src);
                SimpleOpManager disOp = opManager;
                return ImageConversion.toBufferedImage(disOp.process());
            }
        }
//        if (container instanceof View2dContainer) {
//            BufferedImage image = componentToImage(container);
//            return image;
//        }
        return null;
    }

    private static void saveImage(BufferedImage image, String filePath) {
        try {
            // 获取文件扩展名
            String format = "png"; // 可以根据需要修改为其他格式

            // 保存图片到本地文件
            ImageIO.write(image, format, new File(filePath));

            System.out.println("Image saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
