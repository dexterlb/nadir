package org.qtrp.nadir.Database;

/**
 * Created by do on 28/04/17.
 */

public class Roll {
    Long id;
    String name;
    String colour;
    Long timestamp;

    public Roll(){}

    public Roll(Long id, String name, String colour, Long timestamp) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Roll{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", timestamp=" + timestamp +
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
