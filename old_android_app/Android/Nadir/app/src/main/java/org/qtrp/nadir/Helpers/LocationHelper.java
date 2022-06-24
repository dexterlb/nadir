package org.qtrp.nadir.Helpers;

import android.app.Activity;
import android.location.Address;
import android.location.Location;
import android.util.Log;

import java.util.List;
import java.util.Timer;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;

import static java.lang.Thread.sleep;

/**
 * Created by do on 04/05/17.
 */

public class LocationHelper {
    private Activity activity;
    private Location lastLocation;

    private static final String TAG = "LocationHelper";

    private static final int LOCATION_PERMISSION_ID = 1001;


    public LocationHelper(Activity activity) {
        this.activity = activity;
        this.lastLocation = null;
    }

    public interface OnGotLocationListener {
        void OnGotLocation(Location location);
    }

    public void setOnGotLocationListener(OnGotLocationListener onGotLocationListener) {
        this.onGotLocationListener = onGotLocationListener;
    }

    private OnGotLocationListener onGotLocationListener;

    public void start() {
        SmartLocation.with(activity).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (location != null) {
                    Log.v(TAG, "got a location.");
                } else {
                    Log.v(TAG, "got a location, but it is null :(");
                }
                lastLocation = location;
                onGotLocationListener.OnGotLocation(location);
            }
        });

        Log.v(TAG, "started!");
    }

    public void stop() {
        SmartLocation.with(activity).location().stop();

        Log.v(TAG, "stopped!");
    }

    public Double getLatitude() {
        if (lastLocation != null) {
            return lastLocation.getLatitude();
        } else {
            return null;
        }
    }

    public Double getLongitude() {
        if (lastLocation != null) {
            return lastLocation.getLongitude();
        } else {
            return null;
        }
    }

    public interface OnGotAddressListener {
        void OnGotAddress(Location location, String address);
    }

    public void getDummyAddress(final Location location, final OnGotAddressListener callback) {
        callback.OnGotAddress(location, "dummy");
    }

    public void getAddress(Location location, final OnGotAddressListener callback) {
        Log.v(TAG, "getting an address.");
        SmartLocation.with(activity).geocoding().reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location location, List<Address> addresses) {
                        String formattedLocation;

                        if (addresses.isEmpty()) {
                            callback.OnGotAddress(location,null);
                        } else {
                            String address = addresses.get(0).getAddressLine(0);
                            Log.v(TAG, "resolved an address: " + address);
                            callback.OnGotAddress(location, address);
                        }

                    }
                }
        );
    };
}
