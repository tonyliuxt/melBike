package com.homepass.melbike.entitiy;

/**
 * Created by Xintian on 2016/12/7.
 */

public class Coordinates {
    private String type; // "points"
    private Double[] coordinates; // [144.953581, -37.818124]

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }
}
