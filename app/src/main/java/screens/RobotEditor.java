package screens;

import game.Constants;
import game.Robot;
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



public class RobotEditor extends Activity {
	
	private Button addHp, minHp, addShields, minShields, addDamage, minDamage, 
	addMines, minMines, done, resetPoints;
	private TextView hpText, shieldText, damageText, mineText, pointsText;
	private static Robot currRobot;
	private final int HPCHANGE = 5, HPCOST = 1, SHIELDCOST = 1, SHIELDCHANGE = 7,
			DAMCOST = 3, DAMCHANGE = 1, MISSILECOST = 2, MISSILECHANGE = 1;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customizerobot);
        
        // buttons
        addHp = (Button) findViewById(R.id.cust_p_hp);
        addShields = (Button) findViewById(R.id.cust_p_shield);
        addDamage = (Button) findViewById(R.id.cust_p_damage);
        addMines = (Button) findViewById(R.id.cust_p_mines);
        minHp = (Button) findViewById(R.id.cust_m_hp);
        minShields = (Button) findViewById(R.id.cust_m_shield);
        minDamage = (Button) findViewById(R.id.cust_m_damage);
        minMines = (Button) findViewById(R.id.cust_m_mines);
        done = (Button) findViewById(R.id.cust_done);
        resetPoints = (Button) findViewById(R.id.cust_reset);
       
        //text
        hpText = (TextView) findViewById(R.id.cust_hp_val);
        shieldText = (TextView) findViewById(R.id.cust_shield_val);
        damageText = (TextView) findViewById(R.id.cust_damage_val);
        mineText = (TextView) findViewById(R.id.cust_mine_val);
        pointsText = (TextView) findViewById(R.id.cust_points_val);
        
        // initiate text fields to default
        hpText.setText(Constants.ROBOT_START_HP+"");
        shieldText.setText(Constants.ROBOT_START_SHIELDS+"");
        damageText.setText(Constants.ROBOT_START_DAMAGE+"");
        mineText.setText(Constants.ROBOT_START_MISSILES+"");
        pointsText.setText(Constants.ROBOT_BUILD_POINTS+"");
        
        // figure out which screen came from
        Bundle extras = getIntent().getExtras();
        
		if (extras != null) {
			String from = extras.getString("rload_from_key");
			if(from != null) {
				if(from.equals("Home")) {
					currRobot = new Robot();
				}
			}
		}
        
        //init robot
        if(currRobot == null) 
        	currRobot = new Robot();
        
        // upload fields with robot stats
        updateFields();
        
        
        //init listeners for buttons
        addHp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() >= HPCOST) {
					currRobot.setHealth(currRobot.getHealth()+HPCHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()-HPCOST);
					updateFields();
				}
			}
        });
        minHp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() + HPCOST <= Constants.ROBOT_BUILD_POINTS 
						&& currRobot.getHealth()-HPCHANGE >= Constants.ROBOT_START_HP) {
					currRobot.setHealth(currRobot.getHealth()-HPCHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()+HPCOST);
					updateFields();
				}
			}
        });
        addShields.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() >= SHIELDCOST) {
					currRobot.setShield(currRobot.getShield()+SHIELDCHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()-SHIELDCOST);
					updateFields();
				}
			}
        });
        minShields.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() + SHIELDCOST <= Constants.ROBOT_BUILD_POINTS 
						&& currRobot.getShield()-SHIELDCHANGE >= Constants.ROBOT_START_SHIELDS) {
					currRobot.setShield(currRobot.getShield()-SHIELDCHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()+SHIELDCOST);
					updateFields();
				}
			}
        });
        addDamage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() >= DAMCOST) {
					currRobot.setDamage(currRobot.getDamage()+DAMCHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()-DAMCOST);
					updateFields();
				}
			}
        });
        minDamage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() + DAMCOST <= Constants.ROBOT_BUILD_POINTS 
						&& currRobot.getDamage()-DAMCHANGE >= Constants.ROBOT_START_DAMAGE) {
					currRobot.setDamage(currRobot.getDamage()-DAMCHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()+DAMCOST);
					updateFields();
				}
			}
        });
        addMines.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() >= MISSILECOST) {
					currRobot.setMissiles(currRobot.getMissiles()+MISSILECHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()-MISSILECOST);
					updateFields();
				}
			}
        });
        minMines.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() + MISSILECOST <= Constants.ROBOT_BUILD_POINTS 
						&& currRobot.getMissiles()-MISSILECHANGE >= Constants.ROBOT_START_MISSILES) {
					currRobot.setMissiles(currRobot.getMissiles()-MISSILECHANGE);
					currRobot.setBuildPoints(currRobot.getBuildPoints()+MISSILECOST);
					updateFields();
				}
			}
        });
        done.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currRobot.getBuildPoints() > 0) {
					// spend all your points!
					PopUp.makeToast(RobotEditor.this, "Points left to spend!");
				} else {
					startActivity(new Intent(RobotEditor.this, RobotLoader.class));
					finish();
				}
			}
        });
        resetPoints.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				resetAll();
			}
        });
	}
	
	public static Robot getCustomRobot() {
		return currRobot;
	}
	
	public static void setCustomRobot(Robot r) {
		currRobot = r;
	}
	
	private void resetAll() {
		currRobot.setHealth(Constants.ROBOT_START_HP);
		currRobot.setShield(Constants.ROBOT_START_SHIELDS);
		currRobot.setDamage(Constants.ROBOT_START_DAMAGE);
		currRobot.setMissiles(Constants.ROBOT_START_MISSILES);	
		currRobot.setBuildPoints(Constants.ROBOT_BUILD_POINTS);
		updateFields();
	}
	
	private void updateFields() {
		hpText.setText(currRobot.getHealth()+"");
        shieldText.setText(currRobot.getShield()+"");
        damageText.setText(currRobot.getDamage()+"");
        mineText.setText(currRobot.getMissiles()+"");
        pointsText.setText(currRobot.getBuildPoints()+"");
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
