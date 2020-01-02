package screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mbellis.DragNDrop.R;

import dragNDrop.DragNDropListActivity;
import game.Constants;


public class Home extends Activity {
    private Button createButton, loadButton, startButton, quitButton, rLoadButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return;
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Constants.WIDTH = size.x;
        Constants.HEIGHT = size.y;

        createButton = findViewById(R.id.home_create_button);
        loadButton = findViewById(R.id.home_load_button);
        startButton = findViewById(R.id.home_start_button);
        quitButton = findViewById(R.id.home_quit_button);
        rLoadButton = findViewById(R.id.home_rload_button);


        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DragNDropListActivity.content = null;
                startActivity(new Intent(Home.this, DragNDropListActivity.class));
                finish();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ScriptEditor.class));
                finish();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ChooseScript.class));
                finish();
            }
        });

        rLoadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Home.this, RobotLoader.class));
                finish();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                System.exit(0);
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
