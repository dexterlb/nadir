package org.qtrp.nadir.Database;

/**
 * Created by do on 28/04/17.
 */

public class Roll {
    Long id;
    String name;
    String colour;
    Long lastUpdate;

    public Roll(){}

    public Roll(Long id, String name, String colour, Long lastUpdate) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Roll{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", timestamp=" + lastUpdate +
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

    public Long getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
