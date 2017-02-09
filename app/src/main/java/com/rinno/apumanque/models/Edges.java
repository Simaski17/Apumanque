package com.rinno.apumanque.models;

/**
 * Created by simaski on 09-02-17.
 */

public class Edges {

    private int cost;
    private String end;
    private String source;

    public  Edges(){

    }

    public Edges(int cost, String end, String source){
        this.cost = cost;
        this.end = end;
        this.source = source;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
