package org.qtrp.nadir.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import org.qtrp.nadir.CustomViews.ContextMenuRecyclerView;
import org.qtrp.nadir.CustomViews.PhotoAdapter;
import org.qtrp.nadir.Database.FilmRollDbHelper;
import org.qtrp.nadir.Database.Photo;
import org.qtrp.nadir.Helpers.LocationHelper;
import org.qtrp.nadir.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RollActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_ID = 200;
    private static final String TAG = "RollActivity";

    private Button addPhotoButton, resetLocationButton, resetTimeButton;
    private EditText longituteEt, latitudeEt, descriptionEt;
    private TextView timeTv;
    private ContextMenuRecyclerView photoList;
    private FilmRollDbHelper filmRollDbHelper;
    private Long roll_id;
    private PhotoAdapter adapter;
    LinearLayout dummyFocus;
    private Date timestamp;

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private LocationHelper mGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        Intent intent = getIntent();
        roll_id = intent.getLongExtra("roll_id", -1);

        Log.i("Roll id: ", roll_id.toString());
        mGPS = new LocationHelper(this);


        loadUtils();
        bindWidgets();
        setDatasets();
        initLocation();
        setListeners();

        refreshDatasets();

    }

    private void initLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        } else {
            Log.v(TAG,"permissions are fine. starting.");
        }
        mGPS.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_ID: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "location permission granted.");
                } else {
                    Log.v(TAG, "location permission declined");
                }
                initLocation();
            }
        }
    }


    private void refreshDatasets() {
        adapter.clear();
        adapter.addAll(filmRollDbHelper.getPhotosByRollId(roll_id));
        adapter.notifyDataSetChanged();
        reloadAddresses();
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
        dummyFocus = (LinearLayout) findViewById(R.id.dummy_focus);
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

        setTime(getTimeNow());
    }

    private void setListeners() {
        resetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(getTimeNow());
            }
        });

        resetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filmRollDbHelper.insertPhoto(new Photo(
                        null,
                        roll_id,
                        Double.valueOf(latitudeEt.getText().toString()),
                        Double.valueOf(longituteEt.getText().toString()),
                        timestamp.getTime() / 1000,
                        descriptionEt.getText().toString()
                ));

                descriptionEt.getText().clear();

                refreshDatasets();
                dummyFocus.requestFocus();

                InputMethodManager imm =  (InputMethodManager) getSystemService(RollActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(descriptionEt.getWindowToken(), 0);
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager()).setListener(new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date) {
                        setTime(date);
                    }
                })
                        .setInitialDate(timestamp)
                        .build().show();
            }
        });
    }

    private Date getTimeNow(){
        // correct time in the UTC timezone
        // represented as seconds since January 1, 1970 00:00:00 UTC
        return new Date(System.currentTimeMillis());
    }


    private void setTime(Date time){
        timestamp = time;
        timeTv.setText(dateFormat.format(timestamp));
    }

    private void setLocation(){
        latitudeEt.setText(String.format( "%.6f", mGPS.getLatitude() ));
        longituteEt.setText(String.format( "%.6f", mGPS.getLongitude() ));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void reloadAddresses() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            final Photo photo = adapter.getItem(i);
            if (photo.hasAddress()) {
                continue;
            }

            mGPS.getAddress(photo.getLocation(), new LocationHelper.OnGotAddressListener() {
                @Override
                public void OnGotAddress(Location location, String address) {
                    if (address == null) {
                        return;
                    }
                    filmRollDbHelper.updateAddress(location, address);
                    refreshDatasets();
                };
            });
        }
    }
}
