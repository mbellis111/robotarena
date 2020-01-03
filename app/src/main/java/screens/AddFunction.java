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
    private Spinner functionSpinner, targetSpinner, detectedSpinner, directionSpinner;
    private EditText customNumber;
    private Button doneButton, backButton;


    private class StringListener implements OnItemSelectedListener {

        /*
         *  <item>DIRECTION</item>
         *  <item>VARIABLE</item>
         *  <item>CUSTOM NUMBER</item>
         *  NOTHING
         *
         *         <item>MOVE</item>
                    <item>SHOOT</item>
                    <item>RELOAD</item>
                    <item>SHIELD</item>
                    <item>DETECT</item>
                    <item>MISSILE</item>
         */

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String chosen = parent.getItemAtPosition(pos).toString();
            if (parent == functionSpinner) {

                // these do not take a variable
                // these

                // these take a variable
                if(chosen.equals("SHIELD") || chosen.equals("RELOAD") || chosen.equals("DETECT")) {
                    // find the position of the NOTHING option later...
                    targetSpinner.setSelection(0);
                    targetSpinner.setVisibility(View.INVISIBLE);
                    directionSpinner.setVisibility(View.INVISIBLE);
                    detectedSpinner.setVisibility(View.INVISIBLE);
                    customNumber.setVisibility(View.INVISIBLE);
                } else if (chosen.equals("SHOOT") || chosen.equals("MISSILE")) {
                    targetSpinner.setVisibility(View.VISIBLE);
                    directionSpinner.setVisibility(View.VISIBLE);
                    detectedSpinner.setVisibility(View.VISIBLE);
                    customNumber.setVisibility(View.VISIBLE);
                }
            } else if (parent == targetSpinner) {
                if (chosen.equals("DIRECTION")) {
                   directionSpinner.setVisibility(View.VISIBLE);
                   detectedSpinner.setVisibility(View.INVISIBLE);
                   customNumber.setVisibility(View.INVISIBLE);
                } else if (chosen.equals("VARIABLE")) {
                    directionSpinner.setVisibility(View.INVISIBLE);
                    detectedSpinner.setVisibility(View.VISIBLE);
                    customNumber.setVisibility(View.INVISIBLE);
                } else if (chosen.equals("CUSTOM NUMBER")) {
                    directionSpinner.setVisibility(View.INVISIBLE);
                    detectedSpinner.setVisibility(View.INVISIBLE);
                    customNumber.setVisibility(View.VISIBLE);
                } else if (chosen.equals("NOTHING")) {
                    directionSpinner.setVisibility(View.INVISIBLE);
                    detectedSpinner.setVisibility(View.INVISIBLE);
                    customNumber.setVisibility(View.INVISIBLE);
                }
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // nothing to do here
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfunctionview);

        // choose what function to use
        functionSpinner = findViewById(R.id.f_function_spinner);

        // choose what target to use
        targetSpinner = findViewById(R.id.f_target_spinner);

        // chose
        detectedSpinner = findViewById(R.id.f_variable_spinner);
        directionSpinner = findViewById(R.id.f_direction_spinner);
        customNumber = findViewById(R.id.f_custom_text);

        // bottom buttons
        doneButton = findViewById(R.id.f_done_button);
        backButton = findViewById(R.id.f_back_button);


        // function spinner
        ArrayAdapter<?> functionAdapter = ArrayAdapter.createFromResource(this,
                R.array.functions, android.R.layout.simple_spinner_item);
        functionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        functionSpinner.setAdapter(functionAdapter);
        functionSpinner.setOnItemSelectedListener(new StringListener());

        // target spinner
        ArrayAdapter<?> targetAdapator = ArrayAdapter.createFromResource(this,
                R.array.function_targets, android.R.layout.simple_spinner_item);
        targetAdapator.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetSpinner.setAdapter(targetAdapator);
        targetSpinner.setOnItemSelectedListener(new StringListener());

        // direction spinner
        ArrayAdapter<?> directionAdapter = ArrayAdapter.createFromResource(this,
                R.array.direction_variables, android.R.layout.simple_spinner_item);
        directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directionSpinner.setAdapter(directionAdapter);

        // detected spinner
        ArrayAdapter<?> detectedAdaptor = ArrayAdapter.createFromResource(this,
                R.array.detect_variables, android.R.layout.simple_spinner_item);
        detectedAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        detectedSpinner.setAdapter(detectedAdaptor);



        // set up button
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String targetOption = targetSpinner.getSelectedItem().toString();
                if(targetOption.equals("NOTHING")) {
                    ScriptEditor.addValueToStore("NOTHING");
                } else if(targetOption.equals("VARIABLE")) {
                    
                }

                if (variable_button.isChecked()) {
                    final_option = variable_option;
                } else if (direction_button.isChecked()) {
                    final_option = direction_option;
                } else if (text_button.isChecked()) {
                    final_option = editText.getText().toString();
                } else if (null_button.isChecked()) {
                    final_option = "NOTHING";
                }

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
