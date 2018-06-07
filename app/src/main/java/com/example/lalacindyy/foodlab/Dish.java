package com.example.lalacindyy.foodlab;

/**
 * Created by szeng on 4/15/18.
 */

public class Dish{
    private long id;
    private String title;
    private String image;
    private float rating;
    private String ingredients;
    private int time;

    public Dish(long id, String title, String image, float rating){
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
    }

    public long getID(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getImage(){
        return this.image;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }
}
