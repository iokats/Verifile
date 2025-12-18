package com.ykatsatos.hashmaster.hashmaster;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.HexFormat;

public class HashMasterController {

    private static final String SHA_512_ALGORITHM = "SHA-512";
    private static final int maxReadSize = 8192;

    @FXML
    private TextArea hashResultArea;

    @FXML
    protected void onLoadFileButtonClick(final ActionEvent event) {
        // 1. Create the FileChooser
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        // 2. (Optional) Add filters for specific file types
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".dxf", "*.dxf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // 3. Get the current window from the button click event
        final Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();

        // 4. Show the "Open" dialog
        final File selectedFile = fileChooser.showOpenDialog(ownerWindow);

        // 5. Check if a file was selected and process it
        if (selectedFile != null) {

            hashResultArea.setText("Calculating hash...");

            Task<String> task = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return calculateFileChecksum(selectedFile);
                }
            };

            task.setOnSucceeded(e -> hashResultArea.setText(task.getValue()));
            task.setOnFailed(e -> hashResultArea.setText("Error calculating hash."));

            new Thread(task).start();

        } else {
            System.out.println("File selection cancelled.");
        }
    }

    private String calculateFileChecksum(File selectedFile) throws Exception {

        final MessageDigest digest = MessageDigest.getInstance(SHA_512_ALGORITHM);

        try (FileInputStream fileStream = new FileInputStream(selectedFile)) {
            final byte[] byteArray = new byte[maxReadSize];
            int bytesCount = 0;
            while ((bytesCount = fileStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        final byte[] bytes = digest.digest();
        return HexFormat.of().formatHex(bytes);
    }
}
