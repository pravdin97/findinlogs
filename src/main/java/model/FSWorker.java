package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class FSWorker {
    public static String openFile(Path path) {
        String result = "";
        try{
            FileInputStream fstream = new FileInputStream(path.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            while ((strLine = br.readLine()) != null) {
//                System.out.println(strLine);
                result += (strLine + "\n");
            }
            fstream.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return result;
    }
}
