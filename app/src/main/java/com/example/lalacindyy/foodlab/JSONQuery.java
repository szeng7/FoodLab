package com.example.lalacindyy.foodlab;

/**
 * Created by szeng on 4/15/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JSONQuery extends AsyncTask{

    private String[] ingredients;
    private String url;
    private int number;
    private List<Dish> dishes;
    private JSONArray jsonArray;
    private Context context;
    float averageRating;

    public JSONQuery(String[] ingredients, Context context){
        Log.d("Constructor", "in here!");
        this.ingredients = ingredients;
        this.number = ingredients.length;
        this.url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?ranking=1&number=20&ingredients=";
        this.dishes = new ArrayList<Dish>(20);
        this.context = context;
    }

    public void setUrl(){
        this.url += this.ingredients[0];
        if(this.ingredients.length > 1) {
            for (int i = 1; i < this.ingredients.length; i++) {
                this.ingredients[i] = this.ingredients[i].trim();
                this.ingredients[i] = this.ingredients[i].replaceAll("-$|^-", "");
                Log.d("Ingredients", this.ingredients[i]);
                this.url += "%2C+" + this.ingredients[i];
            }
        }
        Log.d("URL", this.url);
    }

    public List<Dish> getDishes(){
        return this.dishes;
    }

    public void addDishes() throws JSONException {
        for(int i = 0; i < 20; i++) {
            JSONObject dish = this.jsonArray.getJSONObject(i);
            String x = dish.toString();
            long id = dish.getInt("id");
            Log.d("Jsonquery1", Long.toString(id));
            String title = dish.getString("title");
            Log.d("Jsonquery1", title);
            String image = dish.getString("image");

            DatabaseReference tipsRef = FirebaseDatabase.getInstance().getReference("ratings").child(Long.toString(id));
            tipsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        averageRating = child.getValue(float.class);
                        Log.d("Jsonquery2", Float.toString(averageRating));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.d("Jsonquery3", Float.toString(averageRating));
            Dish currDish = new Dish(id, title, image, averageRating);
            Log.d("Jsonquery4", Float.toString(averageRating));
            this.dishes.add(i, currDish);
            if (i != 0) {
                this.dishes.get(i - 1).setRating(averageRating);
            }

            String newUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
            newUrl += id;
            newUrl += "/information";

            /*Comment out the try-catch stuff below if you wanna speed up the app*/

            try{
                HttpResponse<JsonNode> response = Unirest.get(newUrl)
                        .header("X-Mashape-Key", "xKJOaJ8LQKmshRRuz5kIywdflh0Ap1Mdb25jsn2vKhLkDctKoq")
                        .header("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com")
                        .asJson();
                JSONArray indJsonArray = response.getBody().getArray();
                JSONObject object = indJsonArray.getJSONObject(0);
                JSONArray ingredients = object.getJSONArray("extendedIngredients");
                int minutes = object.getInt("readyInMinutes");
                currDish.setTime(minutes);
                String ingredientString = "";
                for (int j = 0; j < ingredients.length(); ++j) {
                    JSONObject ingredient = ingredients.getJSONObject(j);
                    ingredientString += ingredient.getString("name");
                    ingredientString += ", ";
                    if (ingredientString.length() > 90) {
                        break;
                    }
                }
                currDish.setIngredients(ingredientString);
            } catch (UnirestException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected Void doInBackground(Object[] params) {

        AppState state = AppState.getInstance();
        try{
            Log.d("TAG", "in here");
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("X-Mashape-Key", "xKJOaJ8LQKmshRRuz5kIywdflh0Ap1Mdb25jsn2vKhLkDctKoq")
                    .header("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com")
                    .asJson();
            if (response.getBody().getArray().length() == 0) {
                exit();
                return null;
            }
            this.jsonArray = response.getBody().getArray();
            this.addDishes();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        AppState state = AppState.getInstance();
        state.setDishes(getDishes());
        Log.d("error", "set dishes");
        super.onPostExecute(o);
        Intent i = new Intent(context, RecipeListActivity.class);
        context.startActivity(i);
    }

    public void exit() {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }
}
