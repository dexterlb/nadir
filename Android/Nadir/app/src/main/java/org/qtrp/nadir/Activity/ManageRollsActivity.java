package org.qtrp.nadir.Activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.qtrp.nadir.CustomViews.AddRollDialogFragment;
import org.qtrp.nadir.CustomViews.ContextMenuRecyclerView;
import org.qtrp.nadir.CustomViews.RollAdapter;
import org.qtrp.nadir.Database.FilmRollDbHelper;
import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.Helpers.SyncHelper;
import org.qtrp.nadir.R;

import java.util.ArrayList;
import java.util.List;

public class ManageRollsActivity extends AppCompatActivity  implements AddRollDialogFragment.NoticeDialogListener {
    SharedPreferences prefereces;
    FloatingActionButton settingsButton;
    FloatingActionButton addButton;
    ContextMenuRecyclerView rollsList;
    FilmRollDbHelper filmRollDbHelper;
    RollAdapter adapter;
    DialogFragment addRollDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rolls);
        setSupportActionBar((Toolbar)findViewById(R.id.action_bar));

        bindWidgets();
        loadUtils();
        setListeners();
        setDatasets();

        registerForContextMenu(rollsList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_sync:
                sync();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sync() {
        final Context context = this;
        Toast.makeText(context, "starting sync", Toast.LENGTH_LONG).show();

        final SyncHelper helper = new SyncHelper(this);
        final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        final Long lastSync = preferences.getLong("lastSync", 0);
        helper.Get(lastSync, new SyncHelper.OnGetListener() {
            @Override
            public void onGotItems(Iterable<SyncHelper.SyncItem> items, Long newSync) {
                final Iterable<SyncHelper.SyncItem> toSync = filmRollDbHelper.forSync(lastSync);
                helper.Push(toSync, new SyncHelper.OnSuccessFailListener() {
                    @Override
                    public void onSuccess() {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("lastSync", lastSync);
                        editor.commit();

                        Toast.makeText(context, "sync successful", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFail(String error) {
                        Toast.makeText(context, "push failed: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, "get failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDatasets() {
        rollsList.setHasFixedSize(true);
        List<Roll> listItems = filmRollDbHelper.getRolls();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rollsList.setLayoutManager(mLayoutManager);
        rollsList.setItemAnimator(new DefaultItemAnimator());

        adapter = new RollAdapter(listItems);
        rollsList.setAdapter(adapter);
    }

    private void refreshDatasets() {
        adapter.clear();
        adapter.addAll(filmRollDbHelper.getRolls());
        adapter.notifyDataSetChanged();
    }

    private void bindWidgets() {
        //settingsButton = (FloatingActionButton) findViewById(R.id.settingsButton);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        rollsList = (ContextMenuRecyclerView) findViewById(R.id.rollsList);
    }

    private void loadUtils() {
        prefereces = PreferenceManager.getDefaultSharedPreferences(this);
        filmRollDbHelper = new FilmRollDbHelper(this);
        addRollDialog = new AddRollDialogFragment();

    }

    private void setListeners(){
//        settingsButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ManageRollsActivity.this, SettingsActivity.class);
//                startActivity(intent);
//            }
//        });

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addRollDialog.show(getFragmentManager(), "Add roll");
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        refreshDatasets();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()) {
            case R.id.delete_roll:
                Roll roll = adapter.getItem(info.position);
                filmRollDbHelper.removeRoll(roll.getId());
                refreshDatasets();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
