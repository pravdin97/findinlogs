package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
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
        if (chosenFile != null && !searchText.getText().equals("")) {
            parent.startSearch(searchText.getText(), Paths.get(chosenFile.toURI()));
            Stage stage = (Stage) searchText.getScene().getWindow();
            stage.close();
        }
    }
}
