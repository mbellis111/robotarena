package game;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

public class ScriptSaver {

    @SuppressLint("WorldReadableFiles")
    public static boolean writeToFile(Context c, ArrayList<String> lines, String fileName) {
        FileOutputStream fOut;
        try {
            fOut = c.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            // Write the string to the file
            for (String s : lines) {
                osw.write(s + "\n");
            }
            /* ensure that everything is
             * really written out and close */
            osw.flush();
            osw.close();
            // if successful, append this to list of saved scripts
            // and if the file name is just being updated
            // UPDATE to save alphabetically?
            if (checkFileAlreadySaved(c, fileName) == false) {
                fOut = c.openFileOutput(Constants.SAVEDSCRIPTS + ".txt", Context.MODE_APPEND);
                osw = new OutputStreamWriter(fOut);
                osw.write(fileName + "\n");
                osw.flush();
                osw.close();
                // terrible way to do it, should save it alphabetically here instead
                // of calling the function to re-write everything...
                arrangeAlphabetically(c, Constants.SAVEDSCRIPTS);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        }
    }

    private static boolean checkFileAlreadySaved(Context c, String fileName) {
        FileInputStream fIn;
        InputStreamReader isr;
        BufferedReader br;
        boolean saved = false;
        try {
            fIn = c.openFileInput(Constants.SAVEDSCRIPTS + ".txt");
            isr = new InputStreamReader(fIn);
            br = new BufferedReader(isr);
            String s = "";
            while ((s = br.readLine()) != null) {
                if (s.equals(fileName)) {
                    saved = true;
                }
            }
            br.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return saved;
    }

    @SuppressLint("WorldReadableFiles")
    public static void removeOneLine(Context c, ArrayList<String> lines, String line) {
        FileOutputStream fOut;
        try {
            fOut = c.openFileOutput(Constants.SAVEDSCRIPTS + ".txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            // Write the string to the file
            for (String s : lines) {
                if (!s.equals(line)) {
                    osw.write(s + "\n");
                }
            }
            /* ensure that everything is
             * really written out and close */
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static boolean deleteAllFiles(Context c) {
        boolean passed = true;
        ArrayList<String> fileNames = readFromFile(c, Constants.SAVEDSCRIPTS);
        if (fileNames == null) {
            return false;
        }
        for (String s : fileNames) {
            if (c.deleteFile(s + ".txt") == false) {
                passed = false;
            }
        }
        if (deleteFile(c, Constants.SAVEDSCRIPTS) == false) {
            return false;
        }
        return passed;
    }

    public static boolean deleteFile(Context c, String fileName) {
        return c.deleteFile(fileName + ".txt");
    }

    public static ArrayList<String> readFromFile(Context c, String fileName) {
        if (fileName.trim().equals("")) {
            return null;
        }
        FileInputStream fIn;
        InputStreamReader isr;
        BufferedReader br;
        ArrayList<String> list = null;
        try {
            fIn = c.openFileInput(fileName + ".txt");
            isr = new InputStreamReader(fIn);
            br = new BufferedReader(isr);
            list = new ArrayList<String>();
            String s = "";
            while ((s = br.readLine()) != null) {
                list.add(s);
            }
            br.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static void arrangeAlphabetically(Context c, String fileName) {
        ArrayList<String> files = readFromFile(c, fileName);
        Collections.sort(files);

        FileOutputStream fOut;
        //now overwrite new alphabetical list to the file
        try {
            fOut = c.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            // Write the files names to the file
            for (String s : files) {
                osw.write(s + "\n");
            }
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
