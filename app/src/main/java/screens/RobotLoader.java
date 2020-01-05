package screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mbellis.RobotArena.R;

import java.util.ArrayList;
import java.util.List;

import fileutils.FileSaver;
import fileutils.RobotSaver;
import game.Constants;
import game.Robot;

public class RobotLoader extends Activity {
    private Spinner saveSpinner;
    private Button editButton, homeButton, deleteButton, deleteAllButton;

    public static final String NEW_ROBOT_TEXT = "-New Robot-";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadrobotlayout);

        saveSpinner = findViewById(R.id.r_save_spinner);
        editButton = findViewById(R.id.r_save_edit_button);
        homeButton = findViewById(R.id.r_save_home_button);
        deleteButton = findViewById(R.id.r_save_delete_button);
        deleteAllButton = findViewById(R.id.r_save_deleteall_button);

        List<String> robots = FileSaver.readFromFile(this, Constants.SAVEDROBOTS);
        if (robots == null) {
            robots = new ArrayList<String>();
        } else {
            // sort alphabetically
            robots = FileSaver.sortAndRemoveDuplicates(robots);
        }

        ArrayAdapter<String> save_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_large);
        save_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saveSpinner.setAdapter(save_adapter);
        resetSpinnerAdapter(robots);

        // select the current robot if it exists
        Robot currentRobot = RobotEditor.getCustomRobot();
        if (currentRobot != null) {
            String robotName = currentRobot.getRobotName();
            int pos = getPositionOfItem(robotName);
            if (pos != -1) {
                saveSpinner.setSelection(pos);
            }
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(RobotLoader.this, Home.class));
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String robotName = getSelectedRobotName();

                if (robotName.equals(NEW_ROBOT_TEXT)) {
                    RobotEditor.setCustomRobot(null);
                } else {
                    Robot r = RobotSaver.getRobotFromFile(RobotLoader.this, robotName);
                    if (r == null) {
                        PopUp.makeToast(RobotLoader.this, "Robot not found!");
                        return;
                    }
                    RobotEditor.setCustomRobot(r);
                }
                Intent intent = new Intent(RobotLoader.this, RobotEditor.class);
                startActivity(intent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String robotName = getSelectedRobotName();

                List<String> robots = FileSaver.readFromFile(RobotLoader.this, Constants.SAVEDROBOTS);
                if (robots == null || !robots.contains(robotName)) {
                    PopUp.makeToast(RobotLoader.this, "Robot not found!");
                    return;
                }

                if (RobotSaver.deleteFile(RobotLoader.this, robotName)) {
                    robots.remove(robotName);
                    FileSaver.writeToFile(RobotLoader.this, robots, Constants.SAVEDROBOTS);
                    resetSpinnerAdapter(robots);
                    PopUp.makeToast(RobotLoader.this, "Robot deleted");
                } else {
                    PopUp.makeToast(RobotLoader.this, "Error deleting robot!");
                }
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(RobotLoader.this);
                alert.setMessage("Confirm deletion of all robots? Cannot be undone!");
                alert.setTitle("Delete all files...");
                alert.setPositiveButton("Ok", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteAllRobots();
                    }
                });

                alert.setNegativeButton("Cancel", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PopUp.makeToast(RobotLoader.this, "Cancelled!");
                    }
                });

                alert.show();
            }
        });
    }

    private String getSelectedRobotName() {
        return saveSpinner.getSelectedItem().toString();
    }

    private void resetSpinnerAdapter(List<String> robots) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
        adapter.clear();
        adapter.add(NEW_ROBOT_TEXT);
        if (robots != null && !robots.isEmpty()) {
            adapter.addAll(robots);
        }
        adapter.notifyDataSetChanged();
    }

    private int getPositionOfItem(String robotName) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
        return adapter.getPosition(robotName);
    }

    public void deleteAllRobots() {
        if (RobotSaver.deleteAllFiles(RobotLoader.this)) {
            resetSpinnerAdapter(null);
            PopUp.makeToast(RobotLoader.this, "All files deleted!");
            return;
        }
        PopUp.makeToast(RobotLoader.this, "Error deleting all files!");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, About.class));
                finish();
                return true;
            case R.id.help:
                startActivity(new Intent(this, Help.class));
                finish();
                return true;
            case R.id.home:
                startActivity(new Intent(this, Home.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // disabled
    }
}
