package game;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class RobotSaver {

    public static Robot getRobotFromFile(Context c, String fileName) {
        if (fileName.trim().equals("")) {
            return null;
        }
        FileInputStream fIn;
        ObjectInputStream oIn;
        Robot r = null;

        try {
            fIn = c.openFileInput(fileName + "R.txt");
            oIn = new ObjectInputStream(fIn);
            r = (Robot) oIn.readObject();
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
        return r;
    }

    public static boolean writeToFile(Context c, Robot r, String fileName) {
        FileOutputStream fOut;
        ObjectOutputStream oOut;
        OutputStreamWriter osw;

        // set the robots name
        r.setRobotName(fileName);

        try {
            fOut = c.openFileOutput(fileName + "R.txt", Context.MODE_PRIVATE);
            oOut = new ObjectOutputStream(fOut);
            // write the robot to the file
            oOut.writeObject(r);
            oOut.flush();
            oOut.close();

            // now write to list of saved robots
            if (!checkFileAlreadySaved(c, fileName)) {
                fOut = c.openFileOutput(Constants.SAVEDROBOTS + ".txt", Context.MODE_APPEND);
                osw = new OutputStreamWriter(fOut);
                osw.write(fileName + "\n");
                osw.flush();
                osw.close();

                // bad way to do it, but it works
                ScriptSaver.arrangeAlphabetically(c, Constants.SAVEDROBOTS);
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
            fIn = c.openFileInput(Constants.SAVEDROBOTS + ".txt");
            isr = new InputStreamReader(fIn);
            br = new BufferedReader(isr);

            String s;
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

    public static boolean deleteAllFiles(Context c) {
        boolean passed = true;
        ArrayList<String> fileNames = ScriptSaver.readFromFile(c, Constants.SAVEDROBOTS);
        if (fileNames == null) {
            return false;
        }
        for (String s : fileNames) {
            if (!c.deleteFile(s + "R.txt")) {
                passed = false;
            }
        }
        if (!ScriptSaver.deleteFile(c, Constants.SAVEDROBOTS)) {
            return false;
        }
        return passed;
    }

    public static boolean deleteFile(Context c, String fileName) {
        return c.deleteFile(fileName + "R.txt");
    }

    public static void removeOneLine(Context c, ArrayList<String> lines, String line) {
        FileOutputStream fOut;
        try {
            fOut = c.openFileOutput(Constants.SAVEDROBOTS + ".txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            for (String s : lines) {
                if (!s.equals(line)) {
                    osw.write(s + "\n");
                }
            }

            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

}
