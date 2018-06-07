package com.example.lalacindyy.foodlab;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;

public class RecipeQuery extends AsyncTask {

    private long recipeID;
    private String url;
    private JSONArray jsonArray;
    private android.support.v4.app.FragmentManager fragmentManager;
    private Intent intent;
    private int fragID;
    private Context context;

    public RecipeQuery(long id, Context context){
        Log.d("Constructor", "in here!");
        this.recipeID = id;
        this.url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
        this.context = context;
    }

    public void setUrl(){
        this.url += recipeID;
        this.url += "/information";
        Log.d("URL", Long.toString(recipeID));
    }

    @Override
    protected Void doInBackground(Object[] params) {

        try{
            Log.d("TAG", "in here");
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("X-Mashape-Key", "xKJOaJ8LQKmshRRuz5kIywdflh0Ap1Mdb25jsn2vKhLkDctKoq")
                    .header("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com")
                    .asJson();
            this.jsonArray = response.getBody().getArray();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        AppState.getInstance().setCurrentRecipe(jsonArray);
        Intent intent = new Intent(context, RecipeActivity.class);
        context.startActivity(intent);
    }
}
