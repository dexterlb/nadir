package org.qtrp.nadir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageRolls extends AppCompatActivity {
    SharedPreferences prefereces;
    FloatingActionButton settingsButton;
    ListView rollsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rolls);

        bindWidgets();
        loadUtils();
        setListeners();
        dummyData();
    }

    private void dummyData() {
        ArrayList<String> listItems = new ArrayList<>();
        listItems.add("Roll 1");
        listItems.add("Roll 2");
        listItems.add("Roll 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        rollsList.setAdapter(adapter);

    }

    private void bindWidgets() {
        settingsButton = (FloatingActionButton) findViewById(R.id.settingsButton);
        rollsList = (ListView) findViewById(R.id.rollsList);
    }

    private void loadUtils() {
        prefereces = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private void setListeners(){
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageRolls.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        rollsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ManageRolls.this, RollActicity.class);
                intent.putExtra("roll_id", position);
                startActivity(intent);
            }
        });
    }

}
