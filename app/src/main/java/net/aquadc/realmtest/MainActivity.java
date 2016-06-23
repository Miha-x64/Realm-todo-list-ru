package net.aquadc.realmtest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    /******** Работа с UI и жизненным циклом ********/
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // находим все Item'ы в базе
        items = realm.where(Item.class).findAll();
        // и создаём адаптер с ними
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        // находим ListView
        ListView listView = (ListView) findViewById(R.id.list);
        assert listView != null;
        // ставим адаптер
        listView.setAdapter(adapter);
        // редактирвоание по клику
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(position);
            }
        });

        // удаление по долгому клику
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showRemoveDialog(position);
                return true;
            }
        });

        // находим большую круглую кнопку
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        // добавление по клику
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }

    private void showAddDialog() {
        // диалог с полем для ввода
        final EditText input = new AppCompatEditText(MainActivity.this);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Create")
                .setView(input)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addItem(input.getText().toString());
                        // костыль: обновить адаптер чуть позже
                        getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        }, 0);
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showEditDialog(final int pos) {
        // ещё диалог с полем для ввода
        final EditText input = new AppCompatEditText(MainActivity.this);
        String text = items.get(pos).getText();
        input.setText(text);                // с текстом
        input.setSelection(text.length());  // и курсором в конце
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Edit")
                .setView(input)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateItem(pos, input.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showRemoveDialog(final int pos) {
        new AlertDialog.Builder(this)
                .setTitle("Remove")
                .setMessage("Remove item?")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(pos);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    // вложенные Item'ы
    // не сложилось

    /******** работа с данными ********/

    private final Realm realm = Realm.getDefaultInstance();
    private RealmResults<Item> items;

    private void addItem(String text) {
        // начать транзакцию
        realm.beginTransaction();
        // создать новый Item
        Item item = realm.createObject(Item.class);
        // поставить текст
        item.setText(text);
        // завершить транзакицию (сохранить)
        realm.commitTransaction();
    }

    private void updateItem(int pos, String text) {
        realm.beginTransaction();
        // изменить текст Item'а
        items.get(pos).setText(text);
        realm.commitTransaction();
    }

    private void removeItem(int pos) {
        realm.beginTransaction();
        // удалить Item
        items.deleteFromRealm(pos);
        realm.commitTransaction();
    }

    // хлам

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
