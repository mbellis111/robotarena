package dragNDrop;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mbellis.DragNDrop.R;

import fileutils.ScriptSaver;
import nodes.Parser;
import nodes.Parser.ErrorMessage;
import nodes.ScriptStore;
import screens.About;
import screens.Add;
import screens.Help;
import screens.Home;
import screens.PopUp;
import screens.ScriptLoader;

public class ScriptEditor extends ListActivity {

    private static ScriptStore scriptStore = null;
    private Button addButton, removeButton, saveButton;

    public static void addValueToStore(String text) {
        if (scriptStore != null) {
            scriptStore.addValue(text);
        }
    }

    public static void resetScriptStore() {
        scriptStore = new ScriptStore();
    }

    public static ScriptStore getScriptStore() {
        return scriptStore;
    }

    public static void setScriptStore(ScriptStore scriptStore) {
        ScriptEditor.scriptStore = scriptStore;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scripteditorview);
        if (scriptStore == null) {
            resetScriptStore();
        }

        setListAdapter(new DragNDropAdapter(this, new int[]{R.layout.dragitem}, new int[]{R.id.TextView01}, scriptStore.getContents()));
        ListView listView = getListView();

        if (listView instanceof DragNDropListView) {
            ((DragNDropListView) listView).setDropListener(mDropListener);
            ((DragNDropListView) listView).setRemoveListener(mRemoveListener);
            ((DragNDropListView) listView).setDragListener(mDragListener);
        }

        addButton = findViewById(R.id.list_addbutton);
        removeButton = findViewById(R.id.list_removebutton);
        saveButton = findViewById(R.id.list_savebutton);

        // set up button listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ScriptEditor.this, Add.class));
                finish();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get rid of the bottom item on the list
                if (scriptStore.getContents().size() != 0) {
                    scriptStore.getContents().remove(scriptStore.getContents().size() - 1);

                    //refresh the screen
                    ListView listView = getListView();
                    if (listView instanceof DragNDropListView) {
                        listView.invalidateViews();
                    }
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String scriptName = nameText.getText().toString();

                // check that the script has a name
                if (scriptName.trim().equals("")) {
                    PopUp.makeToast(ScriptEditor.this, "Script needs a name!");
                    return;
                }

                // check the syntax of the script
                if (scriptStore.getContents() != null && scriptStore.getContents().size() > 0) {
                    Parser p = new Parser(scriptStore.getContents());
                    ErrorMessage error = p.checkForSyntaxErrors();
                    if (error != ErrorMessage.GOOD) {
                        PopUp.makeToast(ScriptEditor.this, p.errorToString(error), Toast.LENGTH_LONG);
                    }
                }

                // save the script
                scriptStore.setScriptName(scriptName);
                if (scriptStore != null && ScriptSaver.saveScriptStore(ScriptEditor.this, scriptStore, scriptName)) {
                    PopUp.makeToast(ScriptEditor.this, "Script saved");
                } else {
                    PopUp.makeToast(ScriptEditor.this, "Error saving script!");
                }

                startActivity(new Intent(ScriptEditor.this, ScriptLoader.class));
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

    private DropListener mDropListener =
            new DropListener() {
                public void onDrop(int from, int to) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof DragNDropAdapter) {
                        ((DragNDropAdapter) adapter).onDrop(from, to);
                        getListView().invalidateViews();
                    }
                }
            };

    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof DragNDropAdapter) {
                        ((DragNDropAdapter) adapter).onRemove(which);
                        getListView().invalidateViews();
                    }
                }
            };

    private DragListener mDragListener =
            new DragListener() {

                int backgroundColor = 0xe0103010;
                int defaultBackgroundColor;

                public void onDrag(int x, int y, ListView listView) {
                    // TODO Auto-generated method stub
                }

                public void onStartDrag(View itemView) {
                    itemView.setVisibility(View.INVISIBLE);
                    defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                    itemView.setBackgroundColor(backgroundColor);
                    ImageView iv = itemView.findViewById(R.id.ImageView01);
                    if (iv != null) {
                        iv.setVisibility(View.INVISIBLE);
                    }
                }

                public void onStopDrag(View itemView) {
                    itemView.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(defaultBackgroundColor);
                    ImageView iv = itemView.findViewById(R.id.ImageView01);
                    if (iv != null) {
                        iv.setVisibility(View.VISIBLE);
                    }
                }

            };

}