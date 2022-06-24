package org.qtrp.nadir.Database;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;
import org.qtrp.nadir.Helpers.SyncHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by do on 30/04/17.
 */

public class Photo extends Syncable implements SyncHelper.SyncItem{
    Long photoId;
    Long rollId;

    Location location;
    Long timestamp;
    String description;

    String address;
    Long lastAddressUpdateTimestamp = Long.valueOf(0);

    FilmRollDbHelper db;

    public Photo(Long photo_id, Long roll_id, Double latitude, Double longitude, Long timestamp, String description, Long lastUpdate, String uniqueId, Integer isDeleted, Integer isSynced, FilmRollDbHelper db) {
        location = new Location("database");
        if (latitude != null && longitude != null) {
            location.setLatitude(latitude);
            location.setLongitude(longitude);
        }
        this.photoId = photo_id;
        this.rollId = roll_id;
        this.location = location;
        this.timestamp = timestamp;
        this.description = description;
        this.lastUpdate = lastUpdate;
        this.isSynced = isSynced;

        this.uniqueId = uniqueId;
        this.isDeleted = isDeleted;

        this.db = db;
    }


    public Long getLastAddressUpdateTimestamp() {
        return lastAddressUpdateTimestamp;
    }

    public void setLastAddressUpdateTimestamp(Long lastAddressUpdateTimestamp) {
        this.lastAddressUpdateTimestamp = lastAddressUpdateTimestamp;
    }

    public Photo(Long photo_id, Long roll_id, Location location, Long timestamp, String description, FilmRollDbHelper db) {
        this.photoId = photo_id;
        this.rollId = roll_id;
        this.location = location;
        this.timestamp = timestamp;
        this.description = description;

        this.db = db;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Long getRollId() {
        return rollId;
    }

    public void setRollId(Long rollId) {
        this.rollId = rollId;
    }

    public Double getLatitude() {
        return location.getLatitude();
    }

    public void setLatitude(Double latitude) {
        this.location.setLatitude(latitude);
    }

    public Double getLongitude() {
        return location.getLongitude();
    }

    public void setLongitude(Double longitude) {
        this.location.setLongitude(longitude);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JSONObject jsonify() throws JSONException{
        JSONObject record = new JSONObject();
//        Location location;
//        Long timestamp;
//        String description;
//        Integer isDeleted;

        record.put("latitude", this.getLatitude());
        record.put("longitude", this.getLongitude());
        record.put("timestamp", this.timestamp);
        record.put("description", this.description);
        record.put("isDeleted", this.isDeleted);
        record.put("roll", db.getRollById(this.getRollId()).getUniqueID());
        record.put("_type_", "photo");
        return record;
    }

    public Photo(JSONObject record, FilmRollDbHelper db) throws JSONException {
        this.db = db;
        this.location = new Location("database");
        this.setLatitude(record.getDouble("latitude"));
        this.setLongitude(record.getDouble("longitude"));
        this.setTimestamp(record.getLong("longitude"));
        this.setDescription(record.getString("description"));
        this.setDeleted(record.getInt("isDeleted"));
        this.setRollId(db.getRollByUniqueId(record.getString("roll")).getId());
    }


    public void setLastUpdate(Long lastUpdate) {this.lastUpdate = lastUpdate;}

    public String getCoordinates() {
        return getCoordinates("#0.00");
    }

    public String getCoordinates(String formatPattern) {
        NumberFormat formatter = new DecimalFormat(formatPattern);

        return
                  formatter.format(getLatitude())
                + ", " + formatter.format(getLongitude());
    }

    public String getFriendlyLocation() {
        if (!hasAddress()) {
            return getCoordinates();
        }
        return address + "(" + getCoordinates() + ")";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean hasAddress() {
        return address != null && !address.isEmpty();
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photoId=" + photoId +
                "rollId=" + rollId +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }

    public boolean mustUpdateAddress() {
        // only try to update address once a day
        return !hasAddress() && lastAddressUpdateTimestamp < (System.currentTimeMillis() / 1000 - 86400);
    }
}


