package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class SearchSettingsController {
    private MainController parent;
    private File chosenFile;

    @FXML
    private TextField pathToFolder, searchText, extension;

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    @FXML
    public void specifyFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("/var/log"));
        File directory = directoryChooser.showDialog(null);
        if (directory != null) {
            pathToFolder.setText(directory.getAbsolutePath());
            chosenFile = directory;
        }
    }

    @FXML
    public void startSearch() {
        if (chosenFile == null)
            try {
                chosenFile = Paths.get(pathToFolder.getText()).toFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        if (chosenFile != null && !searchText.getText().equals("")) {
            parent.startSearch(searchText.getText(), Paths.get(chosenFile.toURI()), extension.getText());
            Stage stage = (Stage) searchText.getScene().getWindow();
            stage.close();
        }
    }
}
