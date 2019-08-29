package model.findinfs;

import javafx.util.Pair;
import model.FSWorker;
import model.search.PatternFinder;
import model.tree.Node;
import model.tree.Tree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class FSTraversal {
    private PatternFinder patternFinder;
    private Path rootDirectory;
    private Tree tree;
    private String extension;

    public FSTraversal(PatternFinder patternFinder, Path rootDirectory) {
        this.patternFinder = patternFinder;
        this.rootDirectory = rootDirectory;
        //по умолчанию
        this.extension = "log";
    }

    public Tree getTree() {
        return tree;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Поиск образца в файле
     * @param file - файл, в котором производится поиск
     */
    private Node searchPattern(File file) {
        String text = FSWorker.openFile(file.toPath());
        // файл прочитался корректно
        if (text.length() != 0) {
            ArrayList<Pair<Integer, Integer>> result = patternFinder.search(text.toCharArray());

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

        if (!directory.canRead())
            return;

        File[] filesInDirectory = directory.listFiles();

        if (filesInDirectory == null)
            return;

        // находим родителя в дереве
        Node parent = tree.findNode(path.toString());

        for (File file: filesInDirectory) {
//            System.out.println(file.getName());

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
                if (!checkExtension(file.getName()))
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

    private boolean checkExtension(String name) {
        String[] split = name.split("\\.");
        if (split[split.length - 1].equals(extension))
            return true;
        else return false;
    }
}
