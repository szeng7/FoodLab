package com.example.lalacindyy.foodlab;

/**
 * Created by szeng on 4/15/18.
 */

import android.app.Application;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class AppState extends Application {
    private static final AppState ourInstance = new AppState();
    private static String TAG = "AppState";

    private String ingredientString;
    private List<Dish> dishes;
    private int position;
    private JSONArray currentRecipe;
    private Recipe recipe;
    private float rating = 0;

    static AppState getInstance() {
        return ourInstance;
    }

    public AppState() {
        if (getInstance() == null) {
            ingredientString = "";
            position = 0;
            currentRecipe = null;
        } else {
            // This shouldn't do anything
        }
    }

    public String getIngredients() {
        return ingredientString;
    }

    public void setingredients(String list) {
        ingredientString = list;
    }

    public void setDishes(List<Dish> list) {
        dishes = list;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setCurrentRecipe(JSONArray arr) {
        currentRecipe = arr;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public Recipe makeRecipe() throws JSONException {
        JSONObject object = currentRecipe.getJSONObject(0);
        String title = object.getString("title");
        int minutes = object.getInt("readyInMinutes");
        long id = object.getInt("id");
        Log.d("APPSTATE", Long.toString(id));
        JSONArray ingredients = object.getJSONArray("extendedIngredients");
        String url = object.getString("sourceUrl");
        JSONArray steps = object.getJSONArray("analyzedInstructions");
        Recipe recipe = new Recipe(title,minutes,id,url,ingredients, steps);
        this.recipe = recipe;
        return recipe;
    }
    
    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
