package org.qtrp.nadir.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import org.qtrp.nadir.CustomViews.ContextMenuRecyclerView;
import org.qtrp.nadir.CustomViews.PhotoAdapter;
import org.qtrp.nadir.Database.FilmRollDbHelper;
import org.qtrp.nadir.Database.Photo;
import org.qtrp.nadir.Helpers.LocationHelper;
import org.qtrp.nadir.R;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RollActivity extends AppCompatActivity {

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 200;
    private Button addPhotoButton, resetLocationButton, resetTimeButton;
    private EditText longituteEt, latitudeEt, descriptionEt;
    private TextView timeTv;
    private ContextMenuRecyclerView photoList;
    private FilmRollDbHelper filmRollDbHelper;
    private Long roll_id;
    private PhotoAdapter adapter;
    LinearLayout dummyFocus;
    private Date timestamp;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
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
        setListeners();

        refreshDatasets();

    }

    private void refreshDatasets() {
        adapter.clear();
        adapter.addAll(filmRollDbHelper.getPhotosByRollId(roll_id));
        adapter.notifyDataSetChanged();
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
        setLocationForSDK();
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
                setLocationForSDK();
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
        timeTv.setText(sdf.format(timestamp));
    }

    private void setLocationForSDK() {
        if(Integer.valueOf(Build.VERSION.SDK_INT) >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.INTERNET,
                                         Manifest.permission.ACCESS_FINE_LOCATION,
                                         Manifest.permission.ACCESS_FINE_LOCATION},
                            ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            } else {
                setLocation();
            }
        } else {
            setLocation();
        }
    }

    private void setLocation(){
        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            latitudeEt.setText(String.format( "%.6f", mGPS.getLatitude() ));
            longituteEt.setText(String.format( "%.6f", mGPS.getLongitude() ));
            mGPS.stopUsingGPS();
        } else {
            Toast.makeText(RollActivity.this, "Can't get location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("sdk", "on result");
        if (requestCode == ASK_MULTIPLE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocation();
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.INTERNET,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                }else{
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
