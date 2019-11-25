package screens;

import com.mbellis.DragNDrop.R;

import dragNDrop.DragNDropListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

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
			if(parent == value_spinner)
				chosen_value = chosen;
			else if (parent == operator_spinner)
				chosen_operator = chosen;
			else if(parent == var_spinner)
				chosen_var = chosen;
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
		
		value_spinner = (Spinner) findViewById(R.id.booleanvaluespinner);
		operator_spinner =(Spinner) findViewById(R.id.booleanoperatorspinner);
		var_spinner = (Spinner) findViewById(R.id.arenaCoordsSpinner);
		done_button = (Button) findViewById(R.id.booleandonebutton);
		number_text = (EditText) findViewById(R.id.booleannumberinput);
		checkBox = (CheckBox) findViewById(R.id.useNumberCheck);
		
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
				String number = number_text.getText().toString();
				if(number == null || number.equals(""))
					number = "0";
				String function;
				if(checkBox.isChecked())
					function = "( "+chosen_value+" "+chosen_operator+" "+number+" )";
				else
					function = "( "+chosen_value+" "+chosen_operator+" "+chosen_var+" )";
				if(dataType.equals("WHILE")) {
					DragNDropListActivity.addStringToList(dataType+" "+function);
					DragNDropListActivity.addStringToList("END_WHILE");
					startActivity(new Intent(AddBoolean.this, DragNDropListActivity.class));
					finish();
				} else if(dataType.equals("IF")){
					DragNDropListActivity.addStringToList(dataType+" "+function);
					DragNDropListActivity.addStringToList("END_IF");
					DragNDropListActivity.addStringToList("ELSE");
					DragNDropListActivity.addStringToList("END_ELSE");
					startActivity(new Intent(AddBoolean.this, DragNDropListActivity.class));
					finish();
				} else {
					// some sort of error
					startActivity(new Intent(AddBoolean.this, DragNDropListActivity.class));
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
		startActivity(new Intent(AddBoolean.this, DragNDropListActivity.class));
		finish();
	}
	
}
