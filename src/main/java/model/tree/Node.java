package model.tree;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node {
    private String filename;
    private String path;
    private Node parent;
    private ArrayList<Node> children;
    private Map<Integer, Pair<Integer, Integer>> foundIndexes;
    private Integer key = 0;
    private boolean isDirectory;

    public Node(String filename, Node parent, ArrayList<Node> children) {
        this.filename = filename;
        this.parent = parent;
        this.children = children;
        foundIndexes = new HashMap<>();
    }

    public Node(String filename) {
        this.filename = filename;
        children = new ArrayList<>();
        foundIndexes = new HashMap<>();
    }

    public void directory() {
        isDirectory = true;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getFilename() {
        return filename;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public Node getParent() {
        return parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void addChild(Node node) {
        if(!children.contains(node))
            children.add(node);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addIndexes(Pair<Integer, Integer> indexes) {
        if (!foundIndexes.containsValue(indexes))
            foundIndexes.put(key++, indexes);
    }

    @Override
    public String toString() {
        return filename;
    }

    public Map<Integer, Pair<Integer, Integer>> getFoundIndexes() {
        return foundIndexes;
    }
}
