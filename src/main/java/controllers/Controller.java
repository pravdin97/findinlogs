package controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import model.findinfs.FSTraversal;
import model.search.PatternFinder;
import model.tree.Node;
import model.tree.Tree;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class Controller {
    @FXML
    private AnchorPane searchResultPane, fileResultPane;

    @FXML
    private InlineCssTextArea textArea;

    @FXML
    public void initialize() {
        textArea.setWrapText(true);
        PatternFinder patternFinder = new PatternFinder("kek");
        FSTraversal fsTraversal = new FSTraversal(patternFinder, Paths.get(System.getProperty("user.home"), "Documents"));
        fsTraversal.search();

        Tree resultTree = fsTraversal.getTree();
        TreeView<Node> treeView = new TreeView<>();
        TreeItem<Node> nodeTreeItem = convertNodeToTreeItem(resultTree.getRoot());
        treeView.setRoot(nodeTreeItem);

        searchResultPane.getChildren().addAll(treeView);

        treeView.getSelectionModel().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {

            TreeItem<Node> selectedItem = (TreeItem<Node>) newValue;
            //debug
            System.out.println("Selected Text : " + selectedItem.getValue());
            showSearchResultInFile(selectedItem.getValue());
        });
    }

    private void showSearchResultInFile(Node node) {
        if (node.isDirectory())
            return;
        String fileText = openFile(node.getPath());
        textArea.clearStyle(0, textArea.getLength());
        textArea.clear();
        textArea.appendText(fileText);
        Map<Integer, Pair<Integer, Integer>> foundIndexes = node.getFoundIndexes();
        for (Integer key: foundIndexes.keySet()) {
            Pair<Integer, Integer> pair = foundIndexes.get(key);
            int begin = pair.getKey(), end = pair.getValue();
            textArea.setStyle(begin, end + 1, "-fx-fill: red;");
        }
    }

    private String openFile(String path) {
        String line, result = "";

        try {
            FileReader fileReader =
                    new FileReader(path);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                result += line;
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.err.println(ex);
        }
        catch(IOException ex) {
            System.err.println(ex);
        }
        return result;
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
}
