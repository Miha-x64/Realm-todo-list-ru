package net.aquadc.realmtest.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.aquadc.realmtest.interaction.FabClient;
import net.aquadc.realmtest.dao.ItemsDao;
import net.aquadc.realmtest.activity.MainActivity;
import net.aquadc.realmtest.R;
import net.aquadc.realmtest.adapter.RealmListAdapter;
import net.aquadc.realmtest.model.Item;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by miha on 16.09.16
 */
public class ItemsFragment extends Fragment implements FabClient {

    /*pkg*/ final ItemsDao itemCtl = new ItemsDao(Realm.getDefaultInstance());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // находим все Item'ы в базе
        final RealmResults<Item> items = itemCtl.findAll();
        // и создаём адаптер с ними
        ListAdapter adapter = new RealmListAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);

        // находим ListView
        ListView listView = (ListView) view.findViewById(R.id.list);
        assert listView != null;
        // ставим адаптер
        listView.setAdapter(adapter);

        // по клику открываем детали
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(items.get(position));
            }
        });
    }

    @Override
    public int getFabIcon() {
        return R.drawable.ic_add_white_24dp;
    }

    @Override
    public void onFabClick() {
        showAddDialog();
    }

    void showDetails(Item item) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(MainActivity.ACTION_ITEM_DETAILS);
        intent.putExtra("item uuid", item.getUuid());
        startActivity(intent);
    }

    private void showAddDialog() {
        // диалог с полем для ввода
        final EditText input = new AppCompatEditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("Create")
                .setView(input)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemCtl.addItem(input.getText().toString());
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
}
