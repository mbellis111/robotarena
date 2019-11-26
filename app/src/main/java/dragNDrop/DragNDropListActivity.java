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

import java.util.ArrayList;

import nodes.Parser;
import nodes.Parser.ErrorMessage;
import screens.About;
import screens.Add;
import screens.Help;
import screens.Home;
import screens.PopUp;
import screens.ScriptEditor;

public class DragNDropListActivity extends ListActivity {

    public static ArrayList<String> content = null;
    private Button addButton, removeButton, saveButton;

    public static void addStringToList(String text) {
        content.add(text);
    }

    public static void resetContent() {
        content = new ArrayList<String>();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dragndroplistview);
        if (content == null) {
            content = new ArrayList<String>();
        }


        setListAdapter(new DragNDropAdapter(this, new int[]{R.layout.dragitem}, new int[]{R.id.TextView01}, content));
        ListView listView = getListView();

        if (listView instanceof DragNDropListView) {
            ((DragNDropListView) listView).setDropListener(mDropListener);
            ((DragNDropListView) listView).setRemoveListener(mRemoveListener);
            ((DragNDropListView) listView).setDragListener(mDragListener);
        }

        addButton = (Button) findViewById(R.id.list_addbutton);
        removeButton = (Button) findViewById(R.id.list_removebutton);
        saveButton = (Button) findViewById(R.id.list_savebutton);


        // set up button listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DragNDropListActivity.this, Add.class));
                finish();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get rid of the bottom item on the list
                if (content.size() != 0) {
                    content.remove(content.size() - 1);

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
                if (content != null && content.size() > 0) {
                    Parser p = new Parser(content);
                    ErrorMessage error = p.checkForSyntaxErrors();
                    if (error != ErrorMessage.GOOD) {
                        PopUp.makeToast(DragNDropListActivity.this, p.errorToString(error), Toast.LENGTH_LONG);
                    } else {
                        startActivity(new Intent(DragNDropListActivity.this, ScriptEditor.class));
                        finish();
                    }
                } else {
                    PopUp.makeToast(DragNDropListActivity.this, "No script to save!");
                }
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
                    ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
                    if (iv != null) {
                        iv.setVisibility(View.INVISIBLE);
                    }
                }

                public void onStopDrag(View itemView) {
                    itemView.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(defaultBackgroundColor);
                    ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
                    if (iv != null) {
                        iv.setVisibility(View.VISIBLE);
                    }
                }

            };

}