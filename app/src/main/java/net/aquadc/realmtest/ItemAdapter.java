package net.aquadc.realmtest;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter<T extends ItemAdapter.Parent<T>> extends ArrayAdapter<T> implements ExpandableListAdapter {

    private final List<T> dataSet;
    private final LayoutInflater inflater;
    private final @LayoutRes int layout;

    public ItemAdapter(Context con, @LayoutRes int layout, List<T> dataSet) {
        super(con, layout, dataSet);
        this.dataSet = dataSet;
        this.layout = layout;
        inflater = LayoutInflater.from(con);
    }

    @Override
    public int getGroupCount() {
        return dataSet.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataSet.get(groupPosition).getChildCount();
    }

    @Override
    public T getGroup(int groupPosition) {
        return dataSet.get(groupPosition);
    }

    @Override
    public T getChild(int groupPosition, int childPosition) {
        return dataSet.get(groupPosition).getChild(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition<<16 | childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getViewFor(groupPosition, -1, convertView, parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getViewFor(groupPosition, childPosition, convertView, parent);
    }

    private View getViewFor(int grPos, int chPos, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = inflater.inflate(layout, parent, false);
        } else {
            view = convertView;
        }

        try {
//            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
//            } else {
                //  Otherwise, find the TextView field within the layout
//                text = (TextView) view.findViewById(mFieldId);
//            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        T item;
        if (chPos < 0) {
            item = getGroup(grPos);
        } else {
            item = getChild(grPos, chPos);
        }
        if (item instanceof CharSequence) {
            text.setText((CharSequence)item);
        } else {
            text.setText(item.toString());
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return getChildId((int) groupId, (int) childId);
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return getGroupId((int) groupId);
    }

    public interface Parent<T> {
        int getChildCount();
        T getChild(int pos);
    }
}
