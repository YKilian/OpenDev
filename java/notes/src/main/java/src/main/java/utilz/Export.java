package src.main.java.utilz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import src.main.java.gui.gui;

public class Export {
    public static void exportAsTXT(String title, String note){
        String filename = "/exports/" + title + ".txt";
        File currentFile = new File(gui.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        File parentDir = currentFile.getParentFile().getParentFile();
        String parentPath = parentDir.getAbsolutePath().replace(File.separator, "/");
        filename = parentPath + filename;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(note);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
