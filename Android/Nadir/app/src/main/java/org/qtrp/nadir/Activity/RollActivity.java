package org.qtrp.nadir.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.qtrp.nadir.CustomViews.ContextMenuRecyclerView;
import org.qtrp.nadir.CustomViews.PhotoAdapter;
import org.qtrp.nadir.Database.FilmRollDbHelper;
import org.qtrp.nadir.Database.Photo;
import org.qtrp.nadir.R;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RollActivity extends AppCompatActivity {

    private Button addPhotoButton, resetLocationButton, resetTimeButton;
    private EditText longituteEt, latitudeEt, descriptionEt;
    private TextView timeTv;
    private ContextMenuRecyclerView photoList;
    private FilmRollDbHelper filmRollDbHelper;
    private Long roll_id;
    private PhotoAdapter adapter;

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        Intent intent = getIntent();
        roll_id = intent.getLongExtra("roll_id", -1);

        Log.i("Roll id: ", roll_id.toString());

        loadUtils();
        bindWidgets();
        setDatasets();
        setListeners();

        dummyData();

    }

    private void dummyData() {
        adapter.addOne(new Photo(null, roll_id, 2.34534, 2.345345, 12324l, "Stuff"));
        adapter.addOne(new Photo(null, roll_id, 2.54644, 2.5677847845, 113346l, "Other stuff"));
        adapter.addOne(new Photo(null, roll_id, 2.54644, 2.5677847845, 11314l, "New stuff"));
        adapter.notifyDataSetChanged();
    }

    private void bindWidgets() {
        addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
        resetLocationButton = (Button) findViewById(R.id.resetLocationButton);
        resetTimeButton = (Button) findViewById(R.id.resetTimeButton);
        longituteEt = (EditText) findViewById(R.id.longtitudeEditText);
        latitudeEt = (EditText) findViewById(R.id.latitudeEditText);
        descriptionEt = (EditText) findViewById(R.id.addDescriptionEditText);
        timeTv = (TextView) findViewById(R.id.photoTimeTextView);
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

        setTime();
    }

    private void setListeners() {
        resetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });

        

    }

    private String getTime(){
        return sdf.format(cal.getTime());
    }

    private void setTime(){
        timeTv.setText(getTime());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
