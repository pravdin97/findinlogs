package model.tree;

import java.util.ArrayList;

public class Tree {
    private Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public Node findNode(String filename) {
        return find(root, filename);
    }

    private Node find(Node curr, String path) {
        Node result = null;
        if (curr.getPath().equals(path))
            return curr;
        else {
            if (curr.isDirectory()) {
                ArrayList<Node> children = curr.getChildren();
                for (int i = 0; result == null && i < children.size(); i++)
                    result = find(children.get(i), path);
            }
        }
        return result;
    }

    public void correct() {
        deleteEmptyDirs(root);
    }

    private void deleteEmptyDirs(Node curr) {
        if (curr.isDirectory()) {
            if (curr.getChildren().size() == 0)
                curr.getParent().getChildren().remove(curr);

            for (Node child: curr.getChildren()) {
                deleteEmptyDirs(child);
            }
        }
    }
}
