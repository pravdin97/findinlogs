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

    private boolean deleteEmptyDirs(Node curr) {
        boolean isDelete = false;
        if (curr.isDirectory()) {
            if (curr.getChildren().size() == 0 && curr.getParent() != null) {
                curr.getParent().getChildren().remove(curr);
                isDelete = true;
            }
            else isDelete = false;

            ArrayList<Node> children = curr.getChildren();
            for (int i = 0; i < children.size(); i++) {
                boolean res = deleteEmptyDirs(children.get(i));
                if (res) i--;
            }
        }
        return isDelete;
    }

    public Node getRoot() {
        return root;
    }
}
