package screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.mbellis.DragNDrop.R;

import dragNDrop.ScriptEditor;

public class AddFunction extends Activity {
    private Spinner function_spinner, variable_spinner, direction_spinner;
    private RadioButton text_button, variable_button, direction_button, null_button;
    private String function_option, variable_option, direction_option, text_option, final_option;
    private Button done_button;
    private EditText editText;

    private class StringListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String chosen = parent.getItemAtPosition(pos).toString();
            if (parent == function_spinner) {
                function_option = chosen;
            } else if (parent == variable_spinner) {
                variable_option = chosen;
            } else if (parent == direction_spinner) {
                direction_option = chosen;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // nothing to do here
        }

    }

    private OnClickListener radio_listener = new OnClickListener() {

        public void onClick(View v) {
            // Perform action on clicks
            RadioButton rb = (RadioButton) v;
            try {
                if (rb.getId() == null_button.getId()) {
                    final_option = "NOTHING";
                }
                if (rb.getId() == text_button.getId()) {
                    text_option = editText.getText().toString();
                    final_option = text_option;
                }
                if (rb.getId() == variable_button.getId()) {
                    final_option = variable_option;
                }
                if (rb.getId() == direction_button.getId()) {
                    final_option = direction_option;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfunctionview);

        function_spinner = findViewById(R.id.function_spinner);
        variable_spinner = findViewById(R.id.variable_spinner);
        direction_spinner = findViewById(R.id.direction_spinner);
        text_button = findViewById(R.id.text_button);
        variable_button = findViewById(R.id.variable_button);
        null_button = findViewById(R.id.null_button);
        direction_button = findViewById(R.id.direction_button);
        done_button = findViewById(R.id.done_button);
        editText = findViewById(R.id.text_box_edit);

        // set up all the spinners
        ArrayAdapter<?> function_adapter = ArrayAdapter.createFromResource(this,
                R.array.functions, android.R.layout.simple_spinner_item);
        function_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        function_spinner.setAdapter(function_adapter);
        function_spinner.setOnItemSelectedListener(new StringListener());

        ArrayAdapter<?> direction_adapter = ArrayAdapter.createFromResource(this,
                R.array.direction_variables, android.R.layout.simple_spinner_item);
        direction_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        direction_spinner.setAdapter(direction_adapter);
        direction_spinner.setOnItemSelectedListener(new StringListener());

        ArrayAdapter<?> variable_adapter = ArrayAdapter.createFromResource(this,
                R.array.detect_variables, android.R.layout.simple_spinner_item);
        variable_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        variable_spinner.setAdapter(variable_adapter);
        variable_spinner.setOnItemSelectedListener(new StringListener());

        // set up radio buttons
        text_button.setOnClickListener(radio_listener);
        variable_button.setOnClickListener(radio_listener);
        null_button.setOnClickListener(radio_listener);
        direction_button.setOnClickListener(radio_listener);

        // set up button
        done_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // do stuff here
                // so send data over to the other app, namely the ScriptEditor

                /*
                 * Testing some things here
                 */
                if (variable_button.isChecked()) {
                    final_option = variable_option;
                } else if (direction_button.isChecked()) {
                    final_option = direction_option;
                } else if (text_button.isChecked()) {
                    final_option = editText.getText().toString();
                } else if (null_button.isChecked()) {
                    final_option = "NOTHING";
                }

                /*
                 * End Testing
                 */

                String function;
                if (final_option == null || final_option.equals("NOTHING")) {
                    function = function_option;
                } else {
                    function = function_option + " " + final_option;
                }
                ScriptEditor.addValueToStore(function);
                startActivity(new Intent(AddFunction.this, ScriptEditor.class));
                finish();
            }
        });

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
        startActivity(new Intent(AddFunction.this, ScriptEditor.class));
        finish();
    }
}
