package screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mbellis.DragNDrop.R;

import java.util.List;

import game.Arena;
import game.StatTracker;

public class GameOver extends Activity {
    private Button homeButton;
    private TextView statText, winnerText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameoverview);

        homeButton = findViewById(R.id.g_done_button);
        winnerText = findViewById(R.id.gameover_winner);
        statText = findViewById(R.id.stat_text);


        Bundle extras = getIntent().getExtras();
        String winner = null;
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

        // populate stat field
        List<StatTracker> stats = Arena.getStats();
        StringBuilder txt = new StringBuilder();
        for (StatTracker st : stats) {
            txt.append(st.toString());
            txt.append("\n\n");
        }
        statText.setText(txt.toString());

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
