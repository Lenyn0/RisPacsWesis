package sample;


import org.weasis.core.api.gui.Image2DViewer;
import org.weasis.core.api.image.SimpleOpManager;
import org.weasis.core.api.image.ZoomOp;
import org.weasis.core.ui.editor.image.ImageViewerPlugin;
import org.weasis.core.ui.editor.image.ViewCanvas;
import org.weasis.dicom.codec.DicomImageElement;
import org.weasis.dicom.viewer2d.EventManager;
import org.weasis.opencv.data.PlanarImage;
import org.weasis.opencv.op.ImageConversion;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ImageManager {

    private static final Logger logger = LoggerManager.getLogger(ImageManager.class);
    public BufferedImage getImage() {
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
        return null;
    }



    public void sendImage(Socket socket, BufferedImage image, String flag) {
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

            logger.info("JSON with image sent to server.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while sending image.", e);
        }
    }
}