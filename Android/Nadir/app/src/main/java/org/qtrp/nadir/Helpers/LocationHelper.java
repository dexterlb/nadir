package org.qtrp.nadir.Helpers;

import android.app.Activity;
import android.location.Address;
import android.location.Location;
import android.util.Log;

import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;

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

    public void start() {
        SmartLocation.with(activity).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                Log.v(TAG, "got a location.");
                lastLocation = location;
            }
        });

        Log.v(TAG, "started!");
    }

    public void stop() {
        SmartLocation.with(activity).location().stop();

        Log.v(TAG, "stopped!");
    }

    public double getLatitude() {
        if (lastLocation != null) {
            return lastLocation.getLatitude();
        } else {
            return -1;
        }
    }

    public double getLongitude() {
        if (lastLocation != null) {
            return lastLocation.getLongitude();
        } else {
            return -1;
        }
    }

    public interface OnGotAddressListener {
        void OnGotAddress(Location location, String address);
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
