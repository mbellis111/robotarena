package screens;

import game.Constants;
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

/*
 THINGS TO DO OR FIX
 
 	X	Fix crash when no values enabled in start screen, or value missing
 
 	X	Rework script creation screen to not need reloading
 
 	X	Make colors actually look pretty in the create script screen
 
 	X	Create a pass-turn option if script potentially detects an infinite loop, do this by counting then number 
 		of complete iterations and then when reaching a certain point, insert a blocking node into the script 
 		which does nothing but ensures the turn ends for that robot, this will fix poorly made scripts 
 		and those that get stuck, causing them to do nothing instead of crashing the game
 	
 	X 	Edit the script class to keep track of whether or not a function was called during the entire runthrough,
 	 	and a number to see how many times in a row that happened. Related to above issue.
 	
 	X 	Fix popup location, put in the middle of the screen for better visibility (not for long one)
 
 	X 	Ensure that editing current script goes to what is unsaved in editor if file name is blank, if saved delete
 	
 	X 	Fix issue with adapters and toasts & confirm dialogs, ensure getting script name after deleting/adding from file!!!
 
 	X	Create longer popup for debugging script creation
 	
 	X	Change instances of PopUp PopUp.makeToast() where appropriate
 	
 	X	Create yes/no AlertDialog for deleting using method calls inside onClick functions for buttons
 	
 	X	Add color or some functionally to better visualize the create script screen (pretty print?)
 
 	X 	Add in missile functionality
 	
 	X	Add damage taken and dealt to stat tracker
 	
 	X	Re-factor ChooseScript to allow robot selection
 	
 	X	Add background to all screens where appropriate
 	
 	X	Re-factor home screen to add new buttons
 	
 	X	Add in a robot customization screen
 	
 	X	Add in a screen to save & edit robots
 	
 	X	Add in functionality to customize robots stat blocks
 	
 	X	Add colored text for each robot
 	
 	X 	Add in tracked statistics to end game screen (#shot, distance moved, accuracy, etc)
 	
 	X	Add "random" starting position (based on # of tokens? script length?
 	
 	X	Ensure app closes all screens when quitting
 	
 	X 	Add in backgrounds for Home & Animated View screens
 	
 	X 	Find a way to detect an infinite loop in the executer, usually when there is only
 		a while or if and nothing after, or a while/if with only nothing, perhaps make this
 		a syntax error in the syntax checker?
 		
 	X	Change each bullet color to match robot
 
 	X	Add in better error checking with messages
 	
 	X	Check for script with just nothing, or a while nothing
 	
 	X 	Add (or let be pressed) a back button when on the add screen (start the intent b/c its stopped)
 	
 	X	Refactor or add in numbers for easy matching on add function screen
 	
 	X	Fix hp text to include robot id
 	
   	X	Add hp text 
 	
 	X 	Check to see if infinite shielding works
 	
 	X 	Arrange scripts by alphabetical order, instead of date added
 	
 	X 	Add a better save script screen when depending on what screen you come from the box is filled in
 	 		
 	X	Quit should terminate the application
 
 	X 	In the add boolean screen, add an option for a number or a variable,
 		for the purpose of using X and Y coordinates for ARENA_WIDTH and ARENA_HEIGHT
 		
 	X	Fix parser, tokenizer and executer to work with variable in the boolean statement
 	
 	X	Fix the bug where a file is saved with the exact same name as another file
 	
 	X 	Create timer to ensure game ends if robots stupid enough not to fight
 		
 	X	Fix the error with the syntax pop-up when checking a script, no need for the
 		activity to end and restart, just have it come up over the screen like
 		in the saving screen part
 		
 	X	Fix saving with no text
 	
 	X 	Fix the error with the starting positions of the robots and the screen width
 		and height
 	
 	X	Fix the scrolling issue with buttons in the creating a script screen so the
 		text doesn't overlap the buttons
 		
 	X	Disable the back button for every screen except home
 	
 	X	Have each robot be a different colors
 		
 	X	Fix the bug when restarting the application when in the game phase
 	
 	X 	Add the home button to the menu to ensure one can always return
 	
 	X 	Actually put useful text into the about screen and the help screen
 	
 	X	Remove the done button from create script screen
 */


public class Home extends Activity {
	private Button createButton, loadButton, startButton, quitButton, rNewButton, rLoadButton;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.homescreen);
	        
	        if(getIntent().getBooleanExtra("Exit me", false)){
	            finish();
	            return;
	        }
	        
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			Constants.WIDTH = size.x;
			Constants.HEIGHT = size.y;
	       
	        createButton = (Button) findViewById(R.id.home_create_button);
	        loadButton = (Button) findViewById(R.id.home_load_button);
	        startButton = (Button) findViewById(R.id.home_start_button);
	        quitButton = (Button) findViewById(R.id.home_quit_button);
	        rNewButton = (Button) findViewById(R.id.home_rnew_button);
	        rLoadButton = (Button) findViewById(R.id.home_rload_button);
	        
	       
	        
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
	        
	        rNewButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(Home.this, RobotEditor.class);
					intent.putExtra("rload_from_key", "Home");
					startActivity(intent);
					finish();
				}
			});
	        
	        rLoadButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					startActivity( new Intent(Home.this, RobotLoader.class));
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
