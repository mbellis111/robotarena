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

import androidx.cardview.widget.CardView;

import com.mbellis.RobotArena.R;

import dragNDrop.ScriptEditor;

public class Add extends Activity {

    CardView functionCard, ifCard, whileCard, nothingCard;
    Button backButton;

    private OnClickListener cardListener = new OnClickListener() {

        public void onClick(View v) {
            CardView cv = (CardView) v;
            Intent intent;
            if (cv == functionCard) {
                intent = new Intent(Add.this, AddFunction.class);
                finish();
            } else if (cv == whileCard) {
                intent = new Intent(Add.this, AddBoolean.class);
                intent.putExtra("add_data_type", "WHILE");
            } else if (cv == ifCard) {
                intent = new Intent(Add.this, AddBoolean.class);
                intent.putExtra("add_data_type", "IF");
            } else if (cv == nothingCard) {
                ScriptEditor.addValueToStore("NOTHING");
                intent = new Intent(Add.this, ScriptEditor.class);
            } else {
                // button not found, do nothing
                return;
            }
            startActivity(intent);
            finish();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);

        functionCard = findViewById(R.id.add_functionCard);
        ifCard = findViewById(R.id.add_ifCard);
        whileCard = findViewById(R.id.add_whileCard);
        nothingCard = findViewById(R.id.add_nothingCard);

        backButton = findViewById(R.id.add_back_button);

        functionCard.setOnClickListener(cardListener);
        ifCard.setOnClickListener(cardListener);
        whileCard.setOnClickListener(cardListener);
        nothingCard.setOnClickListener(cardListener);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Add.this, ScriptEditor.class));
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
        startActivity(new Intent(Add.this, ScriptEditor.class));
        finish();
    }

}
