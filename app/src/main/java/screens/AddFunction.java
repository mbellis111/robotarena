package screens;

import android.app.Activity;
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
import android.widget.TextView;

import com.mbellis.RobotArena.R;

import dragNDrop.ScriptEditor;

public class AddFunction extends Activity {
    private Spinner functionSpinner, targetSpinner, detectedSpinner, directionSpinner;
    private EditText customNumber;
    private TextView selectValueText, targetText;
    private Button doneButton, backButton;


    private class StringListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            showHideItems();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // nothing to do here
        }

    }

    private void showHideItems() {
        // choose to show or hide items based on the combination selected values
        String chosenFunction = functionSpinner.getSelectedItem().toString();

        if (chosenFunction.equals("SHIELD") || chosenFunction.equals("RELOAD") || chosenFunction.equals("DETECT")) {
            // hide all elements regardless of the target
            targetSpinner.setVisibility(View.INVISIBLE);
            directionSpinner.setVisibility(View.INVISIBLE);
            detectedSpinner.setVisibility(View.INVISIBLE);
            customNumber.setVisibility(View.INVISIBLE);
            selectValueText.setVisibility(View.INVISIBLE);
            targetText.setVisibility(View.INVISIBLE);
            targetSpinner.setSelection(getPositionOfNothing());
        } else {
            String chosenTarget = targetSpinner.getSelectedItem().toString();
            targetSpinner.setVisibility(View.VISIBLE);
            targetText.setVisibility(View.VISIBLE);
            if (chosenTarget.equals("DIRECTION")) {
                selectValueText.setVisibility(View.VISIBLE);
                directionSpinner.setVisibility(View.VISIBLE);
                detectedSpinner.setVisibility(View.INVISIBLE);
                customNumber.setVisibility(View.INVISIBLE);
            } else if (chosenTarget.equals("VARIABLE")) {
                selectValueText.setVisibility(View.VISIBLE);
                directionSpinner.setVisibility(View.INVISIBLE);
                detectedSpinner.setVisibility(View.VISIBLE);
                customNumber.setVisibility(View.INVISIBLE);
            } else if (chosenTarget.equals("NUMBER")) {
                selectValueText.setVisibility(View.VISIBLE);
                directionSpinner.setVisibility(View.INVISIBLE);
                detectedSpinner.setVisibility(View.INVISIBLE);
                customNumber.setVisibility(View.VISIBLE);
            } else if (chosenTarget.equals("NOTHING")) {
                selectValueText.setVisibility(View.INVISIBLE);
                directionSpinner.setVisibility(View.INVISIBLE);
                detectedSpinner.setVisibility(View.INVISIBLE);
                customNumber.setVisibility(View.INVISIBLE);
            }
        }

    }

    private int getPositionOfNothing() {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) targetSpinner.getAdapter();
        return adapter.getPosition("NOTHING");
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

        // the text to prompt selecting a value
        selectValueText = findViewById(R.id.f_select_value_text);
        targetText = findViewById(R.id.f_target_text);

        // function spinner
        ArrayAdapter<?> functionAdapter = ArrayAdapter.createFromResource(this, R.array.functions, R.layout.spinner_item);
        functionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        functionSpinner.setAdapter(functionAdapter);
        functionSpinner.setOnItemSelectedListener(new StringListener());

        // target spinner
        ArrayAdapter<?> targetAdapator = ArrayAdapter.createFromResource(this, R.array.function_targets, R.layout.spinner_item);
        targetAdapator.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetSpinner.setAdapter(targetAdapator);
        targetSpinner.setOnItemSelectedListener(new StringListener());

        // direction spinner
        ArrayAdapter<?> directionAdapter = ArrayAdapter.createFromResource(this, R.array.direction_variables, R.layout.spinner_item);
        directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directionSpinner.setAdapter(directionAdapter);

        // detected spinner
        ArrayAdapter<?> detectedAdaptor = ArrayAdapter.createFromResource(this, R.array.detect_variables, R.layout.spinner_item);
        detectedAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        detectedSpinner.setAdapter(detectedAdaptor);


        // set up button
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String action = functionSpinner.getSelectedItem().toString();
                String targetOption = targetSpinner.getSelectedItem().toString();
                String textToAdd = action;
                if (targetOption.equals("VARIABLE")) {
                    textToAdd += " " + detectedSpinner.getSelectedItem().toString();
                } else if (targetOption.equals("DIRECTION")) {
                    textToAdd += " " + directionSpinner.getSelectedItem().toString();
                } else if (targetOption.equals("NUMBER")) {
                    String number = customNumber.getText().toString();
                    if(number.equals("")) {
                        PopUp.makeToast(AddFunction.this, "Must enter a number!");
                        return;
                    }
                    textToAdd += " " + number;
                }
                ScriptEditor.addValueToStore(textToAdd);

                startActivity(new Intent(AddFunction.this, ScriptEditor.class));
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
