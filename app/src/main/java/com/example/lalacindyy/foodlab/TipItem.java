package com.example.lalacindyy.foodlab;

/**
 * Created by szeng on 4/23/18.
 */

public class TipItem {
    private String user;
    private String tip;
    private float rating;

    public TipItem(String user, String tip, float rating) {
        this.user = user;
        this.tip = tip;
        this.rating = rating;
    }

    public String getUser() {
        return this.user;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
