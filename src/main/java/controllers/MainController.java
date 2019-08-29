package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.FSWorker;
import model.findinfs.FSTraversal;
import model.search.PatternFinder;
import model.tree.Node;
import model.tree.Tree;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class MainController {
    @FXML
    private AnchorPane searchResultPane;

    @FXML
    private VirtualizedScrollPane scroll;

    @FXML
    private InlineCssTextArea textArea;

    @FXML
    private Label currentWordLabel;

    private Integer wordKey;
    private Node currNode;

    private TreeView<Node> treeView;

    @FXML
    public void initialize() {
        setupTextArea();
        setupTreeView();
        wordKey = 0;
    }

    private void setupTreeView() {
        treeView = new TreeView<>();
        searchResultPane.getChildren().addAll(treeView);
        treeView.getSelectionModel().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {

            TreeItem<Node> selectedItem = (TreeItem<Node>) newValue;
            //debug
            System.out.println("Selected Text : " + selectedItem.getValue());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showSearchResultInFile(selectedItem.getValue());
                }
            });
        });
    }

    private void setupTextArea() {
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-size: 14pt;\n" +
                "-fx-font-family: \"Segoe UI Light\";");
    }

    public void startSearch(String pattern, Path path, String extension) {
        PatternFinder patternFinder = new PatternFinder(pattern);
        FSTraversal fsTraversal = new FSTraversal(patternFinder, path);
        if (!extension.equals(""))
            fsTraversal.setExtension(extension);
        Platform.runLater(() -> {
            fsTraversal.search();
            Tree resultTree = fsTraversal.getTree();
            TreeItem<Node> nodeTreeItem = convertNodeToTreeItem(resultTree.getRoot());
            setupTreeView();
            treeView.setRoot(nodeTreeItem);
        });
    }

    private void highlightWord(int begin, int end) {
        textArea.setStyle(begin, end, "-rtfx-background-color: orange;");
    }

    private void unhighlightWord(int begin, int end) {
        textArea.setStyle(begin, end, "-rtfx-background-color: white;");
    }

    private void setLabels() {
        currentWordLabel.setText((wordKey + 1) + " / " +
                currNode.getFoundIndexes().size());
    }

    private void moveToWord(int pos) {
        textArea.moveTo(pos);
    }

    @FXML
    public void nextWord() {
        if (currNode == null)
            return;
        Map<Integer, Pair<Integer, Integer>> foundIndexes = currNode.getFoundIndexes();
        if (foundIndexes.size() == 1)
            return;
        textArea.clearStyle(0, textArea.getLength());

        wordKey++;
        if (wordKey == foundIndexes.keySet().size())
            wordKey = 0;
        Pair<Integer, Integer> pair = foundIndexes.get(wordKey);
        highlightWord(pair.getKey(), pair.getValue() + 1);

        setLabels();
        moveToWord(pair.getKey());
    }

    @FXML
    public void previousWord() {
        if (currNode == null)
            return;
        Map<Integer, Pair<Integer, Integer>> foundIndexes = currNode.getFoundIndexes();
        if (foundIndexes.size() == 1)
            return;
        textArea.clearStyle(0, textArea.getLength());

        wordKey--;
        if (wordKey == -1)
            wordKey = foundIndexes.keySet().size() - 1;
        Pair<Integer, Integer> pair = foundIndexes.get(wordKey);
        highlightWord(pair.getKey(), pair.getValue() + 1);

        setLabels();
    }

    @FXML
    public void selectAllWords() {
        if (currNode == null)
            return;
        Map<Integer, Pair<Integer, Integer>> foundIndexes = currNode.getFoundIndexes();
        for (Integer key: foundIndexes.keySet()) {
            Pair<Integer, Integer> pair = foundIndexes.get(key);
            int begin = pair.getKey(), end = pair.getValue();
            highlightWord(begin, end + 1);
        }
    }

    private void showSearchResultInFile(Node node) {
        if (node.isDirectory())
            return;
        currNode = node;
        String fileText = FSWorker.openFile(Paths.get(node.getPath()));

        textArea.clearStyle(0, textArea.getLength());
        textArea.clear();
        textArea.insertText(0, fileText);

        Map<Integer, Pair<Integer, Integer>> foundIndexes = node.getFoundIndexes();
        wordKey = 0;
        Pair<Integer, Integer> pair = foundIndexes.get(wordKey);
        highlightWord(pair.getKey(), pair.getValue() + 1);

        setLabels();
    }

    private TreeItem<Node> convertNodeToTreeItem(Node node) {
        TreeItem<Node> nodeItem = new TreeItem<>(node);

        if (node.isDirectory()) {
            ArrayList<Node> children = node.getChildren();
            for (Node child: children) {
                TreeItem<Node> curr = convertNodeToTreeItem(child);
                nodeItem.getChildren().addAll(curr);
            }
        }

        return nodeItem;
    }

    @FXML
    public void showSearchSettings() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/searchSettings.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchSettingsController searchSettingsController = loader.getController();
        searchSettingsController.setParent(this);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
