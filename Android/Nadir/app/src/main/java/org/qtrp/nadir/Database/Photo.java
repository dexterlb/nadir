package org.qtrp.nadir.Database;

import android.location.Location;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by do on 30/04/17.
 */

public class Photo {
    Long photoId;
    Long rollId;

    Location location;
    Long timestamp;
    String description;

    String address;

    public Photo(){};

    public Photo(Long photo_id, Long roll_id, double latitude, double longitude, Long timestamp, String description) {
        location = new Location("database");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        this.photoId = photo_id;
        this.rollId = roll_id;
        this.location = location;
        this.timestamp = timestamp;
        this.description = description;
    }

    public Photo(Long photo_id, Long roll_id, Location location, Long timestamp, String description) {
        this.photoId = photo_id;
        this.rollId = roll_id;
        this.location = location;
        this.timestamp = timestamp;
        this.description = description;
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
}


