package screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mbellis.DragNDrop.R;

import java.util.ArrayList;

import dragNDrop.ScriptEditor;
import game.Arena;
import game.StatTracker;

public class GameOver extends Activity {
    private Button homeButton;
    private EditText winnerText;
    private TextView statText;
    private String winner = "";
    private ArrayList<StatTracker> stats;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameoverview);

        homeButton = (Button) findViewById(R.id.gameover_home_button);
        winnerText = (EditText) findViewById(R.id.gameover_end_text);
        statText = (TextView) findViewById(R.id.stat_text);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            winner = extras.getString("winner_data");
        }
        if (winner != null) {
            if (winner.equals("1")) {
                winnerText.setText(R.string.win_text);
            } else if (winner.equals("5")) {
                winnerText.setText(R.string.draw_text);
            } else {
                winnerText.setText(String.format(getResources().getString(R.string.enemy_win), winner));
            }
        }

        // set up stat tracking
        stats = Arena.getStats();
        StringBuilder txt = new StringBuilder();
        for (StatTracker st : stats) {
            txt.append(st.toString());
            txt.append("\n\n");
        }
        statText.setText(txt.toString());

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScriptEditor.resetContent();
                startActivity(new Intent(GameOver.this, Home.class));
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
        // disabled
    }
}
