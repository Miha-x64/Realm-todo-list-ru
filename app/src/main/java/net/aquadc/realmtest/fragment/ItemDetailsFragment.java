package net.aquadc.realmtest.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import net.aquadc.realmtest.interaction.FabClient;
import net.aquadc.realmtest.dao.ItemsDao;
import net.aquadc.realmtest.R;
import net.aquadc.realmtest.adapter.RealmListAdapter;
import net.aquadc.realmtest.model.Item;

import io.realm.Realm;

/**
 * Created by miha on 16.09.16
 */
public class ItemDetailsFragment extends Fragment implements FabClient {

    /*pkg*/ final ItemsDao itemCtl = new ItemsDao(Realm.getDefaultInstance());

    Item currentItem;

    private EditText editText;
    /*pkg*/ ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String uuid = getActivity().getIntent().getStringExtra("item uuid");
        if (uuid == null) {
            return inflater.inflate(R.layout.fragment_empty, container, false);
        } else {
            currentItem = itemCtl.findOne(uuid);
            return inflater.inflate(R.layout.fragment_item_details, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (currentItem == null)
            return;

        editText = (EditText) view.findViewById(R.id.input);
        listView = (ListView) view.findViewById(R.id.list);

        editText.setText(currentItem.getText());
        listView.setAdapter(
                new RealmListAdapter<>(getActivity(), android.R.layout.simple_list_item_1, currentItem.getSubItems()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                // обновляем item
                itemCtl.updateItem(currentItem, editText.getText().toString());
                // назад
                getActivity().finish();
                return true;

            case R.id.cancel:
                // выходим без сохранения
                getActivity().finish();
                return true;

            case R.id.remove:
                showItemRemoveDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public int getFabIcon() {
        return R.drawable.ic_add_white_24dp;
    }

    @Override
    public void onFabClick() {
        final EditText et = new AppCompatEditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("Add sub-item")
                .setView(et)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemCtl.addSubItem(currentItem, et.getText().toString());
                    }
                })
                .show();
    }

    private void showItemRemoveDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Remove this item?")
                .setMessage("All its sub-items will be deleted too.")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // убираем адаптер, который засисит от текущего item'а
                        listView.setAdapter(null);

                        // удаляем
                        itemCtl.removeItem(currentItem);

                        // назад
                        getActivity().finish();
                    }
                })
                .show();
    }
}
