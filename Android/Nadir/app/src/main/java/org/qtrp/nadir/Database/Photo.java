package org.qtrp.nadir.Database;

/**
 * Created by do on 30/04/17.
 */

public class Photo {
    Long photo_id;
    Long roll_id;
    Double latitude;
    Double longtitude;
    Long timestamp;
    String description;
    Integer number;

    public Photo(){};

    public Photo(Long photo_id, Long roll_id, Double latitude, Double longtitude, Long timestamp, String description, Integer number) {
        this.photo_id = photo_id;
        this.roll_id = roll_id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.timestamp = timestamp;
        this.description = description;
        this.number = number;
    }

    public Long getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(Long photo_id) {
        this.photo_id = photo_id;
    }

    public Long getRoll_id() {
        return roll_id;
    }

    public void setRoll_id(Long roll_id) {
        this.roll_id = roll_id;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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
                "photo_id=" + photo_id +
                "roll_id=" + roll_id +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", timestamp=" + timestamp +
                ", number=" + number +
                ", description='" + description + '\'' +
                '}';
    }
}


