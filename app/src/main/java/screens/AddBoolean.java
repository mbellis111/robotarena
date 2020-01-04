package screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mbellis.DragNDrop.R;

import dragNDrop.ScriptEditor;

public class AddBoolean extends Activity {
    private String dataType;
    private Spinner valueSpinner, operatorSpinner;
    private Button doneButton, backButton;
    private EditText customNumber;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbooleanview);

        // ok now figure out exactly where we came from
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dataType = extras.getString("add_data_type");
        }

        valueSpinner = findViewById(R.id.b_value_spinner);
        operatorSpinner = findViewById(R.id.b_operator_spinner);
        doneButton = findViewById(R.id.b_done_button);
        backButton = findViewById(R.id.b_back_button);
        customNumber = findViewById(R.id.b_custom_text);

        ArrayAdapter<?> operatorAdapter = ArrayAdapter.createFromResource(this, R.array.operator_variables, R.layout.spinner_item);
        operatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(operatorAdapter);

        ArrayAdapter<?> valueAdapter = ArrayAdapter.createFromResource(this, R.array.value_variables, R.layout.spinner_item);
        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setAdapter(valueAdapter);

        // done button
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String chosenValue = valueSpinner.getSelectedItem().toString();
                String chosenOperator = operatorSpinner.getSelectedItem().toString();
                String number = customNumber.getText().toString();

                if (number.equals("")) {
                    PopUp.makeToast(AddBoolean.this, "Must enter a number!");
                    return;
                }

                String booleanStatement = "( " + chosenValue + " " + chosenOperator + " " + number + " )";
                if (dataType.equals("WHILE")) {
                    ScriptEditor.addValueToStore(dataType + " " + booleanStatement);
                    ScriptEditor.addValueToStore("END_WHILE");
                } else if (dataType.equals("IF")) {
                    ScriptEditor.addValueToStore(dataType + " " + booleanStatement);
                    ScriptEditor.addValueToStore("END_IF");
                    ScriptEditor.addValueToStore("ELSE");
                    ScriptEditor.addValueToStore("END_ELSE");
                }
                startActivity(new Intent(AddBoolean.this, ScriptEditor.class));
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(AddBoolean.this, ScriptEditor.class));
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
        startActivity(new Intent(AddBoolean.this, ScriptEditor.class));
        finish();
    }

}
