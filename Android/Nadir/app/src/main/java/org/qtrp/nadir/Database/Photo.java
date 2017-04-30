package org.qtrp.nadir.Database;

/**
 * Created by do on 30/04/17.
 */

public class Photo {
    Long photoId;
    Long rollId;
    Double latitude;
    Double longtitude;
    Long timestamp;
    String description;

    public Photo(){};

    public Photo(Long photo_id, Long roll_id, Double latitude, Double longtitude, Long timestamp, String description) {
        this.photoId = photo_id;
        this.rollId = roll_id;
        this.latitude = latitude;
        this.longtitude = longtitude;
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
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
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
    public String toString() {
        return "Photo{" +
                "photoId=" + photoId +
                "rollId=" + rollId +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }
}


