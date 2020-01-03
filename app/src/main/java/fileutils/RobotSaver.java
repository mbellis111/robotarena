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
import game.Robot;

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

    public static boolean saveRobot(Context c, Robot r, String fileName) {
        FileOutputStream fOut;
        ObjectOutputStream oOut;

        // set the robots name
        r.setRobotName(fileName);

        try {
            // write the robot to the file
            fOut = c.openFileOutput(fileName + "R.txt", Context.MODE_PRIVATE);
            oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(r);
            oOut.flush();
            oOut.close();

            // now write to list of saved robots
            List<String> existingRobots = FileSaver.readFromFile(c, Constants.SAVEDROBOTS);
            if (existingRobots == null) {
                existingRobots = new ArrayList<String>();
            }
            existingRobots.add(fileName);
            List<String> sortedRobots = FileSaver.sortAndRemoveDuplicates(existingRobots);
            return FileSaver.writeToFile(c, sortedRobots, Constants.SAVEDROBOTS);
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
        return c.deleteFile(fileName + "R.txt");
    }

}
