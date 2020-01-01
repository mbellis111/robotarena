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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mbellis.DragNDrop.R;

import java.util.ArrayList;
import java.util.List;

import game.Constants;
import game.Robot;
import game.RobotSaver;
import game.ScriptSaver;

public class RobotLoader extends Activity {
    private Spinner saveSpinner;
    private EditText fileName;
    private Button editButton, homeButton, saveButton, deleteButton, deleteAllButton;

    private static final String DEFAULT_SELECT_TEXT = "-New Robot-";

    private class StringListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String chosen = parent.getItemAtPosition(pos).toString();
            if (chosen.equals(DEFAULT_SELECT_TEXT)) {
                fileName.setText("");
            } else {
                fileName.setText(chosen);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // nothing to do here
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadrobotlayout);

        saveSpinner = findViewById(R.id.r_save_spinner);
        fileName = findViewById(R.id.r_save_name);
        editButton = findViewById(R.id.r_save_edit_button);
        homeButton = findViewById(R.id.r_save_home_button);
        saveButton = findViewById(R.id.r_save_save_button);
        deleteButton = findViewById(R.id.r_save_delete_button);
        deleteAllButton = findViewById(R.id.r_save_deleteall_button);

        ArrayList<String> robots = ScriptSaver.readFromFile(this, Constants.SAVEDROBOTS);
        if (robots == null) {
            robots = new ArrayList<String>();
        } else {
            // sort alphabetically
            ScriptSaver.arrangeAlphabetically(this, Constants.SAVEDROBOTS);
        }

        ArrayAdapter<String> save_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        save_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saveSpinner.setAdapter(save_adapter);
        saveSpinner.setOnItemSelectedListener(new StringListener());

        resetSpinnerAdapter(robots);

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(RobotLoader.this, Home.class));
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Robot r = RobotSaver.getRobotFromFile(RobotLoader.this, fileName.getText() + "");
                if (r == null) {
                    PopUp.makeToast(RobotLoader.this, "Robot not found!");
                    return;
                }
                RobotEditor.setCustomRobot(r);
                Intent intent = new Intent(RobotLoader.this, RobotEditor.class);
                intent.putExtra("rload_from_key", "RobotLoader");
                startActivity(intent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fn = fileName.getText().toString();
                if (RobotSaver.deleteFile(RobotLoader.this, fn)) {
                    ArrayList<String> robots = ScriptSaver.readFromFile(RobotLoader.this, Constants.SAVEDROBOTS);
                    if (robots != null) {
                        RobotSaver.removeOneLine(RobotLoader.this, robots, fn);
                        // make save spinner blank if coming from edit script screen
                        robots = ScriptSaver.readFromFile(RobotLoader.this, Constants.SAVEDROBOTS);
                    }
                    resetSpinnerAdapter(robots);
                    PopUp.makeToast(RobotLoader.this, "Robot deleted");
                } else {
                    PopUp.makeToast(RobotLoader.this, "Error deleting file!");
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fileName.getText().toString().trim().equals("") || fileName.getText() == null) {
                    PopUp.makeToast(RobotLoader.this, "Robot needs a name!");
                    return;
                }

                if (RobotEditor.getCustomRobot() != null &&
                        RobotSaver.writeToFile(RobotLoader.this, RobotEditor.getCustomRobot(), fileName.getText() + "")) {
                    // update spinner
                    ArrayList<String> robots = ScriptSaver.readFromFile(RobotLoader.this, Constants.SAVEDROBOTS);
                    resetSpinnerAdapter(robots);
                    PopUp.makeToast(RobotLoader.this, "Robot saved");
                    return;
                }
                PopUp.makeToast(RobotLoader.this, "Error saving robot!");
            }
        });
    }

    private void resetSpinnerAdapter(List<String> robots) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
        adapter.clear();
        adapter.add(DEFAULT_SELECT_TEXT);
        fileName.setText("");
        if (robots != null && !robots.isEmpty()) {
            adapter.addAll(robots);
        }
        adapter.notifyDataSetChanged();
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
