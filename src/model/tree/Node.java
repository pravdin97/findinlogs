package model.tree;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node {
    private String filename;
    private Node parent;
    private ArrayList<Node> children;
    private Map<Integer, Pair<Integer, Integer>> foundIndexes;
    private Integer key = 0;

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
}
