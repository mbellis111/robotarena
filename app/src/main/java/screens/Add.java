package screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

import com.mbellis.DragNDrop.R;

import dragNDrop.DragNDropListActivity;
import nodes.Node.BlockType;

public class Add extends Activity {

    private BlockType nodeType;
    private Button button;
    private RadioButton if_button, while_button, nothing_button, function_button;

    private OnClickListener radio_listener = new OnClickListener() {

        public void onClick(View v) {
            // Perform action on clicks
            RadioButton rb = (RadioButton) v;
            try {
                if (rb == if_button) {
                    nodeType = BlockType.IF_THEN_ELSE;
                } else if (rb == while_button) {
                    nodeType = BlockType.WHILE;
                } else if (rb == nothing_button) {
                    nodeType = BlockType.NOTHING;
                } else if (rb == function_button) {
                    nodeType = BlockType.FUNCTION;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);

        nodeType = BlockType.FUNCTION;

        button = (Button) findViewById(R.id.button_add);
        nothing_button = (RadioButton) findViewById(R.id.add_nothing);
        function_button = (RadioButton) findViewById(R.id.add_function);
        if_button = (RadioButton) findViewById(R.id.add_if);
        while_button = (RadioButton) findViewById(R.id.add_while);

        nothing_button.setOnClickListener(radio_listener);
        function_button.setOnClickListener(radio_listener);
        if_button.setOnClickListener(radio_listener);
        while_button.setOnClickListener(radio_listener);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (nodeType) {
                    case FUNCTION:
                        startActivity(new Intent(Add.this, AddFunction.class));
                        finish();
                        return;
                    case WHILE:
                        Intent while_intent = new Intent(Add.this, AddBoolean.class);
                        while_intent.putExtra("add_data_type", "WHILE");
                        startActivity(while_intent);
                        finish();
                        return;
                    case IF_THEN_ELSE:
                        Intent if_intent = new Intent(Add.this, AddBoolean.class);
                        if_intent.putExtra("add_data_type", "IF");
                        startActivity(if_intent);
                        finish();
                        return;
                    case NOTHING:
                        DragNDropListActivity.addStringToList("NOTHING");
                        startActivity(new Intent(Add.this, DragNDropListActivity.class));
                        finish();
                        return;
                    default:
                        startActivity(new Intent(Add.this, DragNDropListActivity.class));
                        finish();
                        return; // do nothing
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
        startActivity(new Intent(Add.this, DragNDropListActivity.class));
        finish();
    }

}
