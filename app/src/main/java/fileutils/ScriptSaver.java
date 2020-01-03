package fileutils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import game.Constants;
import nodes.ScriptStore;

public class ScriptSaver {

    public static ScriptStore getScriptStoreFromFile(Context c, String fileName) {
        if (fileName.trim().equals("")) {
            return null;
        }
        FileInputStream fIn;
        ObjectInputStream oIn;
        ScriptStore scriptStore = null;

        try {
            fIn = c.openFileInput(fileName + "S.txt");
            oIn = new ObjectInputStream(fIn);
            scriptStore = (ScriptStore) oIn.readObject();
            oIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException ie) {
            ie.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return scriptStore;
    }

    public static boolean saveScriptStore(Context c, ScriptStore scriptStore, String fileName) {
        FileOutputStream fOut;
        ObjectOutputStream oOut;

        // set the robots name
        scriptStore.setScriptName(fileName);

        try {
            // write the script to the file
            fOut = c.openFileOutput(fileName + "S.txt", Context.MODE_PRIVATE);
            oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(scriptStore);
            oOut.flush();
            oOut.close();

            // now write to list of saved scripts
            List<String> existingScripts = FileSaver.readFromFile(c, Constants.SAVEDSCRIPTS);
            if (existingScripts == null) {
                existingScripts = new ArrayList<String>();
            }
            existingScripts.add(fileName);
            List<String> sortedScripts = FileSaver.sortAndRemoveDuplicates(existingScripts);
            return FileSaver.writeToFile(c, sortedScripts, Constants.SAVEDSCRIPTS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAllFiles(Context c) {
        boolean passed = true;
        ArrayList<String> fileNames = FileSaver.readFromFile(c, Constants.SAVEDROBOTS);
        if (fileNames == null) {
            return false;
        }
        for (String s : fileNames) {
            if (!deleteFile(c, s)) {
                passed = false;
            }
        }
        if (!FileSaver.deleteFile(c, Constants.SAVEDROBOTS)) {
            return false;
        }
        return passed;
    }

    public static boolean deleteFile(Context c, String fileName) {
        return c.deleteFile(fileName + "S.txt");
    }

}
