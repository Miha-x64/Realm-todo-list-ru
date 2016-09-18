package net.aquadc.realmtest.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmModel;

/**
 * По сути, это упрощённый клон ArrayAdapter
 */
public class RealmListAdapter<T extends RealmModel> extends RealmBaseAdapter<T> implements ListAdapter {

    private final @LayoutRes int layout;
    private final LayoutInflater inflater;

    public RealmListAdapter(@NonNull Context context, @LayoutRes int layout, @Nullable OrderedRealmCollection<T> data) {
        super(context, data);
        this.layout = layout;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        ((TextView) convertView).setText(String.valueOf(getItem(position)));

        return convertView;
    }
}
