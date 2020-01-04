package screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.mbellis.DragNDrop.R;

public class AnimatedActivity extends Activity {
    private static TextView playerHpText, robot2HpText, robot3HpText, robot4HpText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animatedview);
        playerHpText = findViewById(R.id.playerhp);
        robot2HpText = findViewById(R.id.robot2hp);
        robot3HpText = findViewById(R.id.robot3hp);
        robot4HpText = findViewById(R.id.robot4hp);
    }

    public static void setHpText(String hp, int robot) {
        switch (robot) {
            case 1:
                playerHpText.setText(hp);
                break;
            case 2:
                robot2HpText.setText(hp);
                break;
            case 3:
                robot3HpText.setText(hp);
                break;
            case 4:
                robot4HpText.setText(hp);
                break;
            default:
                break;
        }
    }

    public static void removeMyView(Context c) {
        ((Activity) c).finish();
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
