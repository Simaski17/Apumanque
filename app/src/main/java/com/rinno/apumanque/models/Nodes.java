package com.rinno.apumanque.models;

/**
 * Created by simaski on 09-02-17.
 */

public class Nodes {

    private String floor;
    private String id;
    private int locationX;
    private int locationY;
    private int locationZ;
    private String type;

    public Nodes(){

    }

    public Nodes(float locationX, float locationY){
        this.locationX = (int) locationX;
        this.locationY = (int) locationY;
    }

    public Nodes(String floor, String id, int locationX, int locationY, int locationZ, String type){
        this.floor = floor;
        this.id = id;
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationZ = locationZ;
        this.type = type;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(int locationZ) {
        this.locationZ = locationZ;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
