package com.my_app;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChartImageExporter {

    public File saveAsPng(Node node, String cryptoId) throws IOException {
        String fileName = String.format("chart-%s-%d.png", cryptoId, System.currentTimeMillis());
        File file = new File(fileName);

        WritableImage image = node.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            ImageIO.write(bufferedImage, "png", bos);
        }
        return file;
    }
}