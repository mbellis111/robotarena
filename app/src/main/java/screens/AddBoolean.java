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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.mbellis.DragNDrop.R;

import dragNDrop.ScriptEditor;

public class AddBoolean extends Activity {
    private String dataType, chosen_value, chosen_operator, chosen_var;
    private Spinner value_spinner, operator_spinner, var_spinner;
    private Button done_button;
    private EditText number_text;
    private CheckBox checkBox;

    private class StringListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String chosen = parent.getItemAtPosition(pos).toString();
            // do something here
            if (parent == value_spinner) {
                chosen_value = chosen;
            } else if (parent == operator_spinner) {
                chosen_operator = chosen;
            } else if (parent == var_spinner) {
                chosen_var = chosen;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // nothing to do here
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbooleanview);

        chosen_value = "AMMO";
        chosen_operator = "EQUALS";
        chosen_var = "ARENA_WIDTH";
        // ok now figure out exactly where we came from
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dataType = extras.getString("add_data_type");
        }

        value_spinner = findViewById(R.id.booleanvaluespinner);
        operator_spinner = findViewById(R.id.booleanoperatorspinner);
        var_spinner = findViewById(R.id.arenaCoordsSpinner);
        done_button = findViewById(R.id.booleandonebutton);
        number_text = findViewById(R.id.booleannumberinput);
        checkBox = findViewById(R.id.useNumberCheck);

        checkBox.setChecked(true);

        ArrayAdapter<?> operator_adapter = ArrayAdapter.createFromResource(this,
                R.array.operator_variables, android.R.layout.simple_spinner_item);
        operator_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operator_spinner.setAdapter(operator_adapter);
        operator_spinner.setOnItemSelectedListener(new StringListener());

        ArrayAdapter<?> value_adapter = ArrayAdapter.createFromResource(this,
                R.array.value_variables, android.R.layout.simple_spinner_item);
        value_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        value_spinner.setAdapter(value_adapter);
        value_spinner.setOnItemSelectedListener(new StringListener());

        ArrayAdapter<?> var_adapter = ArrayAdapter.createFromResource(this,
                R.array.other_variables, android.R.layout.simple_spinner_item);
        value_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        var_spinner.setAdapter(var_adapter);
        var_spinner.setOnItemSelectedListener(new StringListener());

        // done button
        done_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = number_text.getText().toString().trim();
                if (number.equals("")) {
                    number = "0";
                }
                String function;
                if (checkBox.isChecked()) {
                    function = "( " + chosen_value + " " + chosen_operator + " " + number + " )";
                } else {
                    function = "( " + chosen_value + " " + chosen_operator + " " + chosen_var + " )";
                }
                if (dataType.equals("WHILE")) {
                    ScriptEditor.addValueToStore(dataType + " " + function);
                    ScriptEditor.addValueToStore("END_WHILE");
                    startActivity(new Intent(AddBoolean.this, ScriptEditor.class));
                    finish();
                } else if (dataType.equals("IF")) {
                    ScriptEditor.addValueToStore(dataType + " " + function);
                    ScriptEditor.addValueToStore("END_IF");
                    ScriptEditor.addValueToStore("ELSE");
                    ScriptEditor.addValueToStore("END_ELSE");
                    startActivity(new Intent(AddBoolean.this, ScriptEditor.class));
                    finish();
                } else {
                    // some sort of error
                    startActivity(new Intent(AddBoolean.this, ScriptEditor.class));
                    finish();
                }
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
