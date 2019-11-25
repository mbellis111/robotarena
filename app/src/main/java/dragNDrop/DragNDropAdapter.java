package dragNDrop;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public final class DragNDropAdapter extends BaseAdapter implements RemoveListener, DropListener{

	private int[] mIds;
    private int[] mLayouts;
    private LayoutInflater mInflater;
    private ArrayList<String> mContent;

    public DragNDropAdapter(Context context, ArrayList<String> content) {
        init(context,new int[]{android.R.layout.simple_list_item_1},new int[]{android.R.id.text1}, content);
    }
    
    public DragNDropAdapter(Context context, int[] itemLayouts, int[] itemIDs, ArrayList<String> content) {
    	init(context,itemLayouts,itemIDs, content);
    }

    private void init(Context context, int[] layouts, int[] ids, ArrayList<String> content) {
    	// Cache the LayoutInflate to avoid asking for a new one each time.
    	mInflater = LayoutInflater.from(context);
    	mIds = ids;
    	mLayouts = layouts;
    	mContent = content;
    }
    
    /**
     * The number of items in the list
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        return mContent.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficient to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public String getItem(int position) {
        return mContent.get(position);
    }

    /**
     * Use the array index as a unique id.
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(mLayouts[0], null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(mIds[0]);
            
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(mContent.get(position));
        
        // change color
        changeColor(holder.text);
        // pretty print here

        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }
    
    private void changeColor(TextView t) {
    	String s = t.getText().toString();
    	if(s.startsWith("WHILE") || s.startsWith("END_WHILE")) {
    		t.setTextColor(Color.YELLOW);
    	} else if (s.startsWith("IF") || s.startsWith("END_IF")) {
    		t.setTextColor(Color.WHITE);
    	} else if(s.startsWith("ELSE") || s.startsWith("END_ELSE")) {
    		t.setTextColor(Color.CYAN);
    	} else if(s.startsWith("MOVE")) { 
    		t.setTextColor(Color.MAGENTA);
    	} else if(s.startsWith("SHOOT")) {
    		t.setTextColor(Color.parseColor("#ffa52c")); // orangy
    	} else if(s.startsWith("SHIELD")) {
    		t.setTextColor(Color.GREEN);
    	} else if(s.startsWith("DETECT")) {
    		t.setTextColor(Color.parseColor("#a1a100")); // greenish
    	} else if(s.startsWith("MISSILE")) {
    		t.setTextColor(Color.RED);
    	} else if(s.startsWith("RELOAD")) {
    		t.setTextColor(Color.parseColor("#90a0aa")); // silvery
    	} else if(s.startsWith("NOTHING")) {
    		t.setTextColor(Color.LTGRAY);
    	}
    }

	public void onRemove(int which) {
		if (which < 0 || which > mContent.size()) return;		
		mContent.remove(which);
	}

	public void onDrop(int from, int to) {
		String temp = mContent.get(from);
		mContent.remove(from);
		mContent.add(to,temp);
	}
}