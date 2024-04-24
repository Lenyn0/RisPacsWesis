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

import org.weasis.core.api.media.data.ImageElement;
import org.weasis.core.ui.util.WtoolBar;
import java.awt.image.BufferedImage;;
import java.io.IOException;
import java.net.Socket;

public class SampleToolBar<E extends ImageElement> extends WtoolBar {

    //创建网络管理器、图像管理器、JSON管理器和GUI管理器的实例
    private final NetworkManager networkManager;
    private final ImageManager imageManager;
    private final JsonManager jsonManager;
    private final GuiManager guiManager;
    protected SampleToolBar() {
        super("Sample Toolbar", 500);

        this.networkManager = new NetworkManager();
        this.imageManager = new ImageManager();
        this.jsonManager = new JsonManager();
        this.guiManager = new GuiManager();
        // 添加按钮到工具栏
        addButton("报告生成", "/icon/32x32/报告生成.png", Constants.REPORT_GENERATION);
        addButton("异常检测", "/icon/32x32/异常检测.png", Constants.ANOMALY_DETECTION);
        addButton("检索", "/icon/32x32/检索.png", Constants.SEARCH);
    }
    // 添加按钮到工具栏的方法
    private void addButton(String toolTip, String iconPath, String actionFlag) {
        guiManager.addButton(this, toolTip, iconPath, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof Component) {
                    // 处理按钮点击事件
                    handleAction(actionFlag);
                }
            }
        });
    }
    // 处理按钮点击事件的方法
    private void handleAction(String flag) {
        networkManager.handleAction(flag, () -> {
            try {
                // 创建Socket连接
                Socket socket = new Socket(Config.getSocketAddress(), Config.getSocketPort());
                // 获取图像
                BufferedImage image = imageManager.getImage();
                if (image != null) {
                    // 发送图像
                    imageManager.sendImage(socket, image, flag);
                    // 接收并展示消息
                    jsonManager.receiveAndPrintMessage(socket, flag);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}
