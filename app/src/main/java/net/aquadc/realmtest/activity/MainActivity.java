package net.aquadc.realmtest.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.aquadc.realmtest.R;
import net.aquadc.realmtest.fragment.ItemDetailsFragment;
import net.aquadc.realmtest.fragment.ItemsFragment;
import net.aquadc.realmtest.interaction.FabClient;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_ITEM_DETAILS =
            "net.aquadc.realmtest.activity.MainActivity.ACTION_ITEM_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment _fragment;
        if (savedInstanceState == null) {
            switch (getIntent().getAction()) {
                case ACTION_ITEM_DETAILS:
                    _fragment = new ItemDetailsFragment();
                    break;

                default:
                    _fragment = new ItemsFragment();

            }

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment, _fragment)
                    .commit();
        } else {
            _fragment = getFragmentManager()
                    .findFragmentById(R.id.fragment);
        }
        final Fragment fragment = _fragment;

        // находим большую круглую кнопку
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        if (fragment instanceof FabClient) {
            fab.setImageResource(((FabClient) fragment).getFabIcon());
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FabClient) fragment).onFabClick();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }
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
            Toast.makeText(this, "Пыщь!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
