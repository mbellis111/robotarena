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

import dragNDrop.ScriptEditor;
import fileutils.FileSaver;
import fileutils.ScriptSaver;
import game.Constants;
import nodes.ScriptStore;

public class ScriptLoader extends Activity {
    private Spinner saveSpinner;
    private Button editButton, homeButton, deleteButton, deleteAllButton;

    public static final String NEW_SCRIPT_TEXT = "-New Script-";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadscriptlayout);

        saveSpinner = findViewById(R.id.s_save_spinner);
        editButton = findViewById(R.id.s_save_edit_button);
        homeButton = findViewById(R.id.s_save_home_button);
        deleteButton = findViewById(R.id.s_save_delete_button);
        deleteAllButton = findViewById(R.id.s_save_deleteall_button);

        List<String> scripts = FileSaver.readFromFile(this, Constants.SAVEDSCRIPTS);
        if (scripts == null) {
            scripts = new ArrayList<String>();
        } else {
            // sort alphabetically
           scripts = FileSaver.sortAndRemoveDuplicates(scripts);
        }

        ArrayAdapter<String> save_adapter = new ArrayAdapter<String>(this,  R.layout.spinner_item_large);
        save_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saveSpinner.setAdapter(save_adapter);
        resetSpinnerAdapter(scripts);

        // select the current script if it exists
        ScriptStore currentScript = ScriptEditor.getScriptStore();
        if (currentScript != null) {
            String scriptName = currentScript.getScriptName();
            int pos = getPositionOfItem(scriptName);
            if (pos != -1) {
                saveSpinner.setSelection(pos);
            }
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ScriptLoader.this, Home.class));
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String scriptName = getSelectedScriptName();

                if (scriptName.equals(NEW_SCRIPT_TEXT)) {
                    ScriptEditor.setScriptStore(null);
                } else {
                    ScriptStore scriptStore = ScriptSaver.getScriptStoreFromFile(ScriptLoader.this, scriptName);
                    if (scriptStore == null) {
                        PopUp.makeToast(ScriptLoader.this, "Script not found!");
                        return;
                    }
                    ScriptEditor.setScriptStore(scriptStore);
                }
                Intent intent = new Intent(ScriptLoader.this, ScriptEditor.class);
                startActivity(intent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String scriptName = getSelectedScriptName();

                List<String> scripts = FileSaver.readFromFile(ScriptLoader.this, Constants.SAVEDSCRIPTS);
                if (scriptName == null || scripts == null || !scripts.contains(scriptName)) {
                    PopUp.makeToast(ScriptLoader.this, "Robot not found!");
                    return;
                }

                if (ScriptSaver.deleteFile(ScriptLoader.this, scriptName)) {
                    scripts.remove(scriptName);
                    FileSaver.writeToFile(ScriptLoader.this, scripts, Constants.SAVEDSCRIPTS);
                    resetSpinnerAdapter(scripts);
                    PopUp.makeToast(ScriptLoader.this, "Script deleted");
                } else {
                    PopUp.makeToast(ScriptLoader.this, "Error deleting script!");
                }
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ScriptLoader.this);
                alert.setMessage("Confirm deletion of all scripts? Cannot be undone!");
                alert.setTitle("Delete all files...");
                alert.setPositiveButton("Ok", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteAllScripts();
                    }
                });

                alert.setNegativeButton("Cancel", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PopUp.makeToast(ScriptLoader.this, "Cancelled!");
                    }
                });

                alert.show();
            }
        });
    }

    private String getSelectedScriptName() {
        return saveSpinner.getSelectedItem().toString();
    }

    private void resetSpinnerAdapter(List<String> scripts) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
        adapter.clear();
        adapter.add(NEW_SCRIPT_TEXT);
        if (scripts != null && !scripts.isEmpty()) {
            adapter.addAll(scripts);
        }
        adapter.notifyDataSetChanged();
    }

    private int getPositionOfItem(String scriptName) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
        return adapter.getPosition(scriptName);
    }

    public void deleteAllScripts() {
        if (ScriptSaver.deleteAllFiles(ScriptLoader.this)) {
            resetSpinnerAdapter(null);
            PopUp.makeToast(ScriptLoader.this, "All files deleted!");
            return;
        }
        PopUp.makeToast(ScriptLoader.this, "Error deleting all files!");
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
