package org.qtrp.nadir.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.qtrp.nadir.CustomListView.RollAdapter;
import org.qtrp.nadir.Database.FilmRollDbHelper;
import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.R;

import java.util.ArrayList;

public class ManageRollsActivity extends AppCompatActivity {
    SharedPreferences prefereces;
    FloatingActionButton settingsButton;
    FloatingActionButton addButton;
    ListView rollsList;
    FilmRollDbHelper filmRollDbHelper;
    ArrayAdapter<Roll> adapter;

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
        ArrayList<Roll> listItems = filmRollDbHelper.getRolls();

        adapter = new RollAdapter(this, listItems);
        rollsList.setAdapter(adapter);
    }

    private void bindWidgets() {
        //settingsButton = (FloatingActionButton) findViewById(R.id.settingsButton);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        rollsList = (ListView) findViewById(R.id.rollsList);
    }

    private void loadUtils() {
        prefereces = PreferenceManager.getDefaultSharedPreferences(this);
        filmRollDbHelper = new FilmRollDbHelper(this);
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
                Roll roll = new Roll(null, "Vitosha", "colour", 24234234);
                filmRollDbHelper.insertRoll(roll);
                adapter.add(roll);
                adapter.notifyDataSetChanged();
            }
        });

        rollsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ManageRollsActivity.this, RollActivity.class);
                intent.putExtra("roll_id", position);
                startActivity(intent);
            }
        });
    }

}
