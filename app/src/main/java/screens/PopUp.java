package screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mbellis.DragNDrop.R;

import dragNDrop.DragNDropListActivity;

public class PopUp extends Activity {
    private Button okButton;
    private EditText textBox;
    private String fromClass;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupview);

        okButton = findViewById(R.id.popup_done_button);
        textBox = findViewById(R.id.popuptext);
        fromClass = "";

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            textBox.setText(extras.getString("popup_key"));
            fromClass = extras.getString("popup_fromscreen");
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fromClass.equals("DragNDropListActivity")) {
                    startActivity(new Intent(PopUp.this, DragNDropListActivity.class));
                } else if (fromClass.equals("ScriptEditor")) {
                    startActivity(new Intent(PopUp.this, ScriptEditor.class));
                } else if (fromClass.equals("ChooseScript")) {
                    startActivity(new Intent(PopUp.this, ChooseScript.class));
                } else if (fromClass.equals("RobotLoader")) {
                    startActivity(new Intent(PopUp.this, RobotLoader.class));
                } else if (fromClass.equals("RobotEditor")) {
                    startActivity(new Intent(PopUp.this, RobotEditor.class));
                } else {
                    startActivity(new Intent(PopUp.this, Home.class));
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // disabled
    }

    public static void makeToast(Context c, String message) {
        Toast alert = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        alert.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        alert.show();
    }

    public static void makeToast(Context c, String message, int duration) {
        Toast alert = Toast.makeText(c, message, duration);
        alert.show();
    }


}
