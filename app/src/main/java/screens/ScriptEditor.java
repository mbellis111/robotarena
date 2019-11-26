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

import dragNDrop.DragNDropListActivity;
import game.Constants;
import game.ScriptSaver;

public class ScriptEditor extends Activity {
    private Spinner saveSpinner;
    private EditText fileName;
    private Button editButton, homeButton, saveButton, deleteButton, deleteAllButton;

    private class StringListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String chosen = parent.getItemAtPosition(pos).toString();
            if (chosen.equals("-Select Script-")) {
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
        setContentView(R.layout.loadscriptlayout);

        saveSpinner = (Spinner) findViewById(R.id.save_spinner);
        fileName = (EditText) findViewById(R.id.save_name);
        editButton = (Button) findViewById(R.id.save_edit_button);
        homeButton = (Button) findViewById(R.id.save_home_button);
        saveButton = (Button) findViewById(R.id.save_save_button);
        deleteButton = (Button) findViewById(R.id.save_delete_button);
        deleteAllButton = (Button) findViewById(R.id.save_deleteall_button);

        ArrayList<String> scripts = ScriptSaver.readFromFile(this, Constants.SAVEDSCRIPTS);
        if (scripts == null) {
            scripts = new ArrayList<String>();
        } else {
            // sort alphabetically
            ScriptSaver.arrangeAlphabetically(this, Constants.SAVEDSCRIPTS);
        }

        ArrayAdapter<String> save_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        // make save spinner blank if coming from edit script screen
        save_adapter.add("-Select Script-");
        for (String s : scripts) {
            save_adapter.add(s);
        }
        save_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saveSpinner.setAdapter(save_adapter);
        saveSpinner.setOnItemSelectedListener(new StringListener());
        fileName.setText("");

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ScriptEditor.this, Home.class));
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fileName.getText() != null && !fileName.getText().toString().trim().equals("")) // if filename is blank keep what was in the content before
                {
                    DragNDropListActivity.content = ScriptSaver.readFromFile(ScriptEditor.this, fileName.getText() + "");
                }
                startActivity(new Intent(ScriptEditor.this, DragNDropListActivity.class));
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fn = fileName.getText().toString();

                if (ScriptSaver.deleteFile(ScriptEditor.this, fn)) {
                    ArrayList<String> scripts = ScriptSaver.readFromFile(ScriptEditor.this, Constants.SAVEDSCRIPTS);
                    ScriptSaver.removeOneLine(ScriptEditor.this, scripts, fn);
                    @SuppressWarnings("unchecked")
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
                    adapter.clear();
                    // make save spinner blank if coming from edit script screen
                    adapter.add("-Select Script-");
                    //scripts = ScriptSaver.readFromFile(ScriptEditor.this, Constants.SAVEDSCRIPTS);
                    scripts.remove(fn);
                    for (String s : scripts) {
                        adapter.add(s);
                    }
                    adapter.notifyDataSetChanged();
                    PopUp.makeToast(ScriptEditor.this, "File Deleted");
                    return;
                }
                PopUp.makeToast(ScriptEditor.this, "Error deleting file");
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // create alert dialog and make button functionality here
                AlertDialog.Builder alert = new AlertDialog.Builder(ScriptEditor.this);
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
                        PopUp.makeToast(ScriptEditor.this, "Cancelled!");
                    }
                });

                alert.show();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fileName.getText().toString().trim().equals("") || fileName.getText() == null) {
                    PopUp.makeToast(ScriptEditor.this, "File needs a name!");
                    return;
                }
                if (DragNDropListActivity.content != null &&
                        DragNDropListActivity.content.size() > 0 &&
                        ScriptSaver.writeToFile(ScriptEditor.this, DragNDropListActivity.content, fileName.getText() + "")) {
                    // update spinner
                    ArrayList<String> scripts = ScriptSaver.readFromFile(ScriptEditor.this, Constants.SAVEDSCRIPTS);
                    @SuppressWarnings("unchecked")
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
                    adapter.clear();
                    // make save spinner blank if coming from edit script screen
                    fileName.setText("");
                    adapter.add("-Select Script-");
                    for (String s : scripts) {
                        adapter.add(s);
                    }
                    adapter.notifyDataSetChanged();
                    PopUp.makeToast(ScriptEditor.this, "File Saved");
                    DragNDropListActivity.resetContent();
                    return;
                }
                PopUp.makeToast(ScriptEditor.this, "Error saving file");
            }
        });
    }

    public void deleteAllScripts() {
        // call static method with this in it, after decision is made
        if (ScriptSaver.deleteAllFiles(ScriptEditor.this)) {
            @SuppressWarnings("unchecked")
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSpinner.getAdapter();
            adapter.clear();
            adapter.add("-Select Script-");
            adapter.notifyDataSetChanged();
            fileName.setText("");
            PopUp.makeToast(ScriptEditor.this, "All files deleted");
            return;
        }
        PopUp.makeToast(ScriptEditor.this, "All files deleted!");
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
