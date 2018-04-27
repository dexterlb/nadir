package org.qtrp.nadir.Database;

import org.json.JSONException;
import org.json.JSONObject;
import org.qtrp.nadir.Helpers.SyncHelper;

/**
 * Created by do on 28/04/17.
 */

public class Roll implements SyncHelper.SyncItem{
    Long id;
    String name;
    String colour;
    Long lastUpdate;
    String uniqueId;

    public Roll(){}

    public Roll(Long id, String name, String colour, Long lastUpdate, String uniqueId) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.lastUpdate = lastUpdate;
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        return "Roll{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", timestamp=" + lastUpdate +
                ", uniqueId=" + uniqueId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setUniqueId(String uniqueId) {this.uniqueId = uniqueId;}

    public String getUniqueId() {return this.uniqueId;}

    public Long getLastUpdate() {
        return this.lastUpdate;
    }

    @Override
    public String getUniqueID() {
        return this.getUniqueId();
    }

    @Override
    public JSONObject jsonify() throws JSONException{
//        String name;
//        String colour;
        JSONObject record = new JSONObject();
        record.put("name", this.name);
        record.put("colour", this.colour);

        return record;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
