package com.example.lalacindyy.foodlab;

/**
 * Created by szeng on 4/15/18.
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {

    private String title;
    private int time;
    private long id;
    private String sourceURL;
    private JSONArray ingredients;
    private JSONArray instructions;

    public Recipe(String title, int time, long id, String source, JSONArray ingredients, JSONArray steps) {
        this.title = title;
        this.id = id;
        this.time = time;
        this.sourceURL = source;
        this.ingredients = ingredients;
        this.instructions = steps;
    }

    public String getTitle() {
        return title;
    }

    public long getID() {
        return id;
    }

    public String[] getIngredients() throws JSONException {
        String[] output = new String[ingredients.length()];
        for (int i = 0; i < ingredients.length(); ++i) {
            JSONObject ingredient = ingredients.getJSONObject(i);
            output[i] = ingredient.getString("original");
        }
        return output;
    }

    public String[] getSteps() throws JSONException {
        JSONObject instructionSet = instructions.getJSONObject(0);
        JSONArray steps = instructionSet.getJSONArray("steps");
        String[] output = new String[steps.length()];
        for (int i = 0; i < steps.length(); ++i) {
            JSONObject step = steps.getJSONObject(i);
            String outputString = Integer.toString(i+1) + ". ";
            outputString += step.getString("step");
            output[i] = outputString;
        }
        return output;
    }

}
