package com.my_app;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChartImageExporter {

    public File saveAsPng(Stage stage, Node node, String cryptoId, String days, String currency) throws IOException {
        String defaultFileName = String.format("chart-%s-%s-days-%s-%d.png", cryptoId, days, currency.toUpperCase(), System.currentTimeMillis());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart as PNG");
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return null;
        }

        WritableImage image = node.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            ImageIO.write(bufferedImage, "png", fos);
        }

        return file;
    }
}