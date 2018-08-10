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
import java.util.UUID;
import java.util.logging.Logger;

public class RollActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_ID = 200;
    private static final String TAG = "RollActivity";

    private Button addPhotoButton, resetLocationButton, resetTimeButton, cancelButton, deletePhotoButton, savePhotoButton;
    private EditText longituteEt, latitudeEt, descriptionEt;
    private TextView timeTv;
    private ContextMenuRecyclerView photoList;
    private FilmRollDbHelper filmRollDbHelper;
    private Long roll_id;
    private PhotoAdapter adapter;
    LinearLayout dummyFocus;
    private Date timestamp;
    private LinearLayout editPhotoButtonsLayout;
    private LinearLayout newPhotoButtonsLayout;

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private LocationHelper mGPS;
    private Photo editingPhoto;

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

        snapMode();
        setListeners();

        refreshDatasets();

    }

    private void editMode(Photo photo) {
        adapter.setCanSelect(false);
        editPhotoButtonsLayout.setVisibility(View.VISIBLE);
        newPhotoButtonsLayout.setVisibility(View.GONE);

        descriptionEt.setText(photo.getDescription());
        setLocation(photo.getLatitude(), photo.getLongitude());
        setTime(new Date(photo.getTimestamp() * 1000));

        editingPhoto = photo;
    }

    private void snapMode() {
        adapter.setCanSelect(true);
        adapter.deselect();
        editingPhoto = null;
        newPhotoButtonsLayout.setVisibility(View.VISIBLE);
        editPhotoButtonsLayout.setVisibility(View.GONE);
        latitudeEt.setText("");
        longituteEt.setText("");
        setLocation();
        descriptionEt.setText("");
        setTime(getTimeNow());

        refreshDatasets();
        dummyFocus.requestFocus();

        InputMethodManager imm =  (InputMethodManager) getSystemService(RollActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(descriptionEt.getWindowToken(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocation();
    }

    private void startLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        } else {
            Log.v(TAG,"permissions are fine. starting.");
        }
        mGPS.start();
    }

    private void stopLocation() {
        mGPS.stop();
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
                startLocation();
            }
        }
    }


    private void refreshDatasets() {
        adapter.clear();
        adapter.addAll(filmRollDbHelper.getPhotosByRollId(roll_id));
        adapter.notifyDataSetChanged();
        reloadAddresses();
    }

//    private void dummyData() {
//        adapter.addOne(new Photo(null, roll_id, 2.34534, 2.345345, 12324l, "Stuff", getTimestamp(), ));
//        adapter.addOne(new Photo(null, roll_id, 2.54644, 2.5677847845, 113346l, "Other stuff", getTimestamp()));
//        adapter.addOne(new Photo(null, roll_id, 2.54644, 2.5677847845, 11314l, "New stuff", getTimestamp()));
//        adapter.notifyDataSetChanged();
//    }

    private void bindWidgets() {
        addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
        resetLocationButton = (Button) findViewById(R.id.resetLocationButton);
        resetTimeButton = (Button) findViewById(R.id.resetTimeButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        deletePhotoButton = (Button) findViewById(R.id.deletePhotoButton);
        savePhotoButton = (Button) findViewById(R.id.savePhotoButton);
        longituteEt = (EditText) findViewById(R.id.longtitudeEditText);
        latitudeEt = (EditText) findViewById(R.id.latitudeEditText);
        descriptionEt = (EditText) findViewById(R.id.addDescriptionEditText);
        timeTv = (TextView) findViewById(R.id.photoTimeTextView);
        photoList = (ContextMenuRecyclerView) findViewById(R.id.photoList);
        dummyFocus = (LinearLayout) findViewById(R.id.dummy_focus);
        editPhotoButtonsLayout = (LinearLayout) findViewById(R.id.editPhotoButtonsLayout);
        newPhotoButtonsLayout = (LinearLayout) findViewById(R.id.newPhotoButtonsLayout);
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

    private Photo setFields(Long photo_id, Long roll_id, String uniqueId, Integer isDeleted){
        return new Photo(
                photo_id,
                roll_id,
                parseDouble(latitudeEt.getText().toString()),
                parseDouble(longituteEt.getText().toString()),
                timestamp.getTime() / 1000,
                descriptionEt.getText().toString(),
                System.currentTimeMillis(),
                uniqueId,
                isDeleted,
                0,
                filmRollDbHelper
        );
    };

    private void setListeners() {
        mGPS.setOnGotLocationListener(new LocationHelper.OnGotLocationListener() {
            @Override
            public void OnGotLocation(Location location) {
                if (latitudeEt.getText().length() == 0 && longituteEt.getText().length() == 0) {
                    setLocation();
                }
            }
        });

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
                filmRollDbHelper.insertPhoto(setFields(
                        null,
                        roll_id,
                        UUID.randomUUID().toString(),
                        0)
                );
                snapMode();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snapMode();
            }
        });

        savePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingPhoto = setFields(editingPhoto.getPhotoId(), editingPhoto.getRollId(), editingPhoto.getUniqueID(), editingPhoto.getDeleted());


                filmRollDbHelper.updatePhoto(editingPhoto);
                snapMode();
            }
        });


        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filmRollDbHelper.removePhoto(editingPhoto.getPhotoId(), System.currentTimeMillis());
                snapMode();
                refreshDatasets();
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

        adapter.setOnItemSelectedListener(new PhotoAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                editMode(adapter.getItem(position));
            }
        });
    }

    private Long getTimestamp() {
        return System.currentTimeMillis() / 1000;
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

    private void setLocation() {
        if (mGPS.getLongitude() != null && mGPS.getLatitude() != null) {
            setLocation(mGPS.getLatitude(), mGPS.getLongitude());
        }
    }

    private void setLocation(Double latitude, Double longitude) {
        latitudeEt.setText(String.format("%.6f", latitude));
        longituteEt.setText(String.format("%.6f", longitude));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void reloadAddresses() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            final Photo photo = adapter.getItem(i);
            if (!photo.mustUpdateAddress()) {
                continue;
            }

            filmRollDbHelper.setLastAddressUpdateTimestamp(photo.getPhotoId(), System.currentTimeMillis() / 1000);

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

    private Double parseDouble(String s) {
        try {
            return Double.valueOf(s);
        } catch(NumberFormatException e) {
            return null;
        }
    }
}
