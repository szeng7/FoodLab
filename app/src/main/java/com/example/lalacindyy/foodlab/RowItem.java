package com.example.lalacindyy.foodlab;

/**
 * Created by szeng on 4/15/18.
 */

public class RowItem {
    private String title;
    private String imageUrl;
    private String ingredients;
    private float rating;
    private int time;
    private Long id;

    public RowItem(Long id, String title, String imageUrl, String ingredients, float rating, int time) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.rating = rating;
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return this.rating;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Long getID() {
        return this.id;
    }
}
