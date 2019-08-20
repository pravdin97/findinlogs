package model.findinfs;

import javafx.util.Pair;
import model.FSWorker;
import model.search.PatternFinder;
import model.tree.Node;
import model.tree.Tree;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FSTraversal {
    private PatternFinder patternFinder;
    private Path rootDirectory;
    private Tree tree;

    public FSTraversal(PatternFinder patternFinder, Path rootDirectory) {
        this.patternFinder = patternFinder;
        this.rootDirectory = rootDirectory;
    }

    public Tree getTree() {
        return tree;
    }

    private String openFile(File file) {
        String string = "";
        try {
            if (file.canRead()) {
                string = new String(Files.readAllBytes(file.toPath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * Поиск образца в файле
     * @param file - файл, в котором производится поиск
     */
    private Node searchPattern(File file) {
//        String text = openFile(file);
        String text = FSWorker.openFile(file.toPath());
        // файл прочитался корректно
        if (text.length() != 0) {
            ArrayList<Pair<Integer, Integer>> result = patternFinder.search(text.toCharArray());
//            ArrayList<Pair<Integer, Integer>> result = patternFinder.stockSearch(text.toString());

            // в файле найден образец
            if (result.size() != 0) {
                Node node = new Node(file.getName());
                node.setPath(file.getPath());

                for (Pair pair: result) {
                    node.addIndexes(pair);
                }

                return node;
            }
        }

        return null;
    }

    /**
     * Обход файлов в файловой системе
     * @param path путь до очередного файла или папки в файловой системе
     */
    private void traversal(Path path) {
        File directory = path.toFile();
        File[] filesInDirectory = directory.listFiles();

        // находим родителя в дереве
        Node parent = tree.findNode(path.toString());

        for (File file: filesInDirectory) {
            System.out.println(file.getName());

            if (file.isDirectory()) {
                // создаем новый узел
                Node newDir = new Node(file.getName());
                newDir.setPath(file.getPath());
                newDir.directory();

                if (parent != null) {
                    parent.addChild(newDir);
                    newDir.setParent(parent);
                }

                traversal(file.toPath());
            }
            else {
                if (!checkExtension(file.getName(), "log"))
                    continue;
                Node node = searchPattern(file);
                if (node != null) {
                    parent.addChild(node);
                    node.setParent(parent);
                }
            }
        }
    }

    public void search() {
        Node root = new Node(rootDirectory.getFileName().toString());
        root.setPath(rootDirectory.toString());
        root.directory();
        tree = new Tree(root);

        traversal(rootDirectory);

        tree.correct();
    }

    private boolean checkExtension(String name, String extension) {
        String[] split = name.split("\\.");
        if (split[split.length - 1].equals(extension))
            return true;
        else return false;
    }
}
