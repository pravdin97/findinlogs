package model.findinfs;

import java.io.File;
import java.nio.file.*;

public class FSTraversal {
    public static void traversal(Path path) {
        File directory = path.toFile();
        File[] filesInDirectory = directory.listFiles();

        for (File file: filesInDirectory) {
            System.out.println(file.getName());


            if (file.isDirectory())
                traversal(file.toPath());
        }
    }

    public static void main(String args[]) {
        Path path = Paths.get(System.getProperty("user.home"), "Documents");
        traversal(path);
    }
}
