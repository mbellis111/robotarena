package fileutils;

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
import java.util.HashSet;
import java.util.List;

public class FileSaver {

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
            String s;
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

    public static List<String> sortAndRemoveDuplicates(List<String> items) {
        if(items == null) {
            return null;
        }
        List<String> uniques = new ArrayList<String>(new HashSet<String>(items));
        Collections.sort(uniques);
        return uniques;
    }

    public static boolean deleteFile(Context c, String fileName) {
        return c.deleteFile(fileName + ".txt");
    }

    public static boolean writeToFile(Context c, List<String> lines, String fileName) {
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
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        }
    }

}
