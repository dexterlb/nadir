package org.qtrp.nadir.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.qtrp.nadir.CustomViews.AddRollDialogFragment;
import org.qtrp.nadir.CustomViews.ContextMenuRecyclerView;
import org.qtrp.nadir.CustomViews.PhotoAdapter;
import org.qtrp.nadir.CustomViews.RollAdapter;
import org.qtrp.nadir.Database.FilmRollDbHelper;
import org.qtrp.nadir.Database.Photo;
import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.R;

import java.util.List;

import static org.qtrp.nadir.R.id.rollsList;

public class RollActivity extends AppCompatActivity {

    private FloatingActionButton addButton;
    private ContextMenuRecyclerView photoList;
    private FilmRollDbHelper filmRollDbHelper;
    private Long roll_id;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_acticity);

        Intent intent = getIntent();
        roll_id = intent.getLongExtra("roll_id", -1);

        Log.i("Roll id: ", roll_id.toString());

        loadUtils();
        bindWidgets();
        setDatasets();

    }

    private void bindWidgets() {
        //settingsButton = (FloatingActionButton) findViewById(R.id.settingsButton);
        //addButton = (FloatingActionButton) findViewById(R.id.addPhotoButton);
        photoList = (ContextMenuRecyclerView) findViewById(R.id.photoList);
    }

    private void loadUtils() {
        filmRollDbHelper = new FilmRollDbHelper(this);
    }

    public void setDatasets(){
        photoList.setHasFixedSize(true);
        List<Photo> listItems = filmRollDbHelper.getPhotosByRollId(roll_id);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        photoList.setLayoutManager(mLayoutManager);
        photoList.setItemAnimator(new DefaultItemAnimator());

        adapter = new PhotoAdapter(listItems);
        photoList.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
