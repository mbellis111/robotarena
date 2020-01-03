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
import android.widget.Spinner;
import android.widget.Toast;

import com.mbellis.DragNDrop.R;

import java.util.ArrayList;
import java.util.List;

import fileutils.FileSaver;
import game.Constants;
import game.Robot;
import fileutils.RobotSaver;
import fileutils.ScriptSaver;
import nodes.ScriptStore;

public class ChooseScript extends Activity {
    public static ScriptStore playerScript, enemy1Script, enemy2Script, enemy3Script;
    public static Robot playerRobot, enemy1Robot, enemy2Robot, enemy3Robot;
    private Spinner playerSpinner, enemy1Spinner, enemy2Spinner, enemy3Spinner,
            playerRSpinner, enemy1RSpinner, enemy2RSpinner, enemy3RSpinner;
    private Button doneButton, homeButton;

    public static void resetChosenScripts() {
        playerScript = null;
        enemy1Script = null;
        enemy2Script = null;
        enemy3Script = null;
    }

    public static void resetChosenRobots() {
        playerRobot = null;
        enemy1Robot = null;
        enemy2Robot = null;
        enemy3Robot = null;
    }

    private class StringListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String chosen = parent.getItemAtPosition(pos).toString();
            ScriptStore scriptStore = ScriptSaver.getScriptStoreFromFile(ChooseScript.this, chosen);
            if (parent == playerSpinner) {
                playerScript = scriptStore;
            } else if (parent == enemy1Spinner) {
                enemy1Script = scriptStore;
            } else if (parent == enemy2Spinner) {
                enemy2Script = scriptStore;
            } else if (parent == enemy3Spinner) {
                enemy3Script = scriptStore;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // nothing to do here
        }
    }


    private class StringRListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String chosen = parent.getItemAtPosition(pos).toString();
            Robot r = RobotSaver.getRobotFromFile(ChooseScript.this, chosen);
            if (parent == playerRSpinner) {
                playerRobot = r;
            } else if (parent == enemy1RSpinner) {
                enemy1Robot = r;
            } else if (parent == enemy2RSpinner) {
                enemy2Robot = r;
            } else if (parent == enemy3RSpinner) {
                enemy3Robot = r;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // nothing to do here
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosescriptview);

        resetChosenScripts();
        resetChosenRobots();

        doneButton = findViewById(R.id.choose_done_button);
        homeButton = findViewById(R.id.choose_home_button);

        playerSpinner = findViewById(R.id.choose_player_spinner);
        enemy1Spinner = findViewById(R.id.choose_enemy1_spinner);
        enemy2Spinner = findViewById(R.id.choose_enemy2_spinner);
        enemy3Spinner = findViewById(R.id.choose_enemy3_spinner);

        playerRSpinner = findViewById(R.id.choose_player_r_spinner);
        enemy1RSpinner = findViewById(R.id.choose_enemy1_r_spinner);
        enemy2RSpinner = findViewById(R.id.choose_enemy2_r_spinner);
        enemy3RSpinner = findViewById(R.id.choose_enemy3_r_spinner);


        List<String> robots = FileSaver.readFromFile(this, Constants.SAVEDROBOTS);
        if (robots == null) {
            robots = new ArrayList<String>();
        }

        List<String> scripts = FileSaver.readFromFile(this, Constants.SAVEDSCRIPTS);
        if (scripts == null) {
            scripts = new ArrayList<String>();
        }

        ArrayAdapter<String> save_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        for (String s : scripts) {
            save_adapter.add(s);
        }
        save_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> save_r_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        for (String s : robots) {
            save_r_adapter.add(s);
        }
        save_r_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playerSpinner.setAdapter(save_adapter);
        playerSpinner.setOnItemSelectedListener(new StringListener());

        enemy1Spinner.setAdapter(save_adapter);
        enemy1Spinner.setOnItemSelectedListener(new StringListener());

        enemy2Spinner.setAdapter(save_adapter);
        enemy2Spinner.setOnItemSelectedListener(new StringListener());

        enemy3Spinner.setAdapter(save_adapter);
        enemy3Spinner.setOnItemSelectedListener(new StringListener());

        playerRSpinner.setAdapter(save_r_adapter);
        playerRSpinner.setOnItemSelectedListener(new StringRListener());

        enemy1RSpinner.setAdapter(save_r_adapter);
        enemy1RSpinner.setOnItemSelectedListener(new StringRListener());

        enemy2RSpinner.setAdapter(save_r_adapter);
        enemy2RSpinner.setOnItemSelectedListener(new StringRListener());

        enemy3RSpinner.setAdapter(save_r_adapter);
        enemy3RSpinner.setOnItemSelectedListener(new StringRListener());


        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //ensure values are filled
                boolean good = true;
                try {
                    if (playerSpinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                    if (enemy1Spinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                    if (enemy2Spinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                    if (enemy3Spinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                    if (playerRSpinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                    if (enemy1RSpinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                    if (enemy2RSpinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                    if (enemy3RSpinner.getItemAtPosition(0).toString().trim().equals("")) {
                        good = false;
                    }
                } catch (IndexOutOfBoundsException e) {
                    good = false;
                }

                if (!good) {
                    PopUp.makeToast(ChooseScript.this, "Fill out all values!", Toast.LENGTH_LONG);
                    return;
                }

                //check scripts
                if (playerScript == null) {
                    playerScript = ScriptSaver.getScriptStoreFromFile(ChooseScript.this, playerSpinner.getItemAtPosition(0).toString());
                }
                if (enemy1Script == null) {
                    enemy1Script = ScriptSaver.getScriptStoreFromFile(ChooseScript.this, enemy1Spinner.getItemAtPosition(0).toString());
                }
                if (enemy2Script == null) {
                    enemy2Script = ScriptSaver.getScriptStoreFromFile(ChooseScript.this, enemy2Spinner.getItemAtPosition(0).toString());
                }
                if (enemy3Script == null) {
                    enemy3Script = ScriptSaver.getScriptStoreFromFile(ChooseScript.this, enemy3Spinner.getItemAtPosition(0).toString());
                }

                // check robots
                if (playerRobot == null) {
                    playerRobot = RobotSaver.getRobotFromFile(ChooseScript.this, playerRSpinner.getItemAtPosition(0).toString());
                }
                if (enemy1Robot == null) {
                    enemy1Robot = RobotSaver.getRobotFromFile(ChooseScript.this, enemy1RSpinner.getItemAtPosition(0).toString());
                }
                if (enemy2Robot == null) {
                    enemy2Robot = RobotSaver.getRobotFromFile(ChooseScript.this, enemy2RSpinner.getItemAtPosition(0).toString());
                }
                if (enemy3Robot == null) {
                    enemy3Robot = RobotSaver.getRobotFromFile(ChooseScript.this, enemy3RSpinner.getItemAtPosition(0).toString());
                }

                if (playerScript == null || enemy1Script == null || enemy2Script == null || enemy3Script == null
                        || playerRobot == null || enemy1Robot == null || enemy2Robot == null || enemy3Robot == null) {
                    PopUp.makeToast(ChooseScript.this, "Fill out all values!");
                    return;
                }
                startActivity(new Intent(ChooseScript.this, AnimatedActivity.class));
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ChooseScript.this, Home.class));
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
