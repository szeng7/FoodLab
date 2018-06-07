package com.example.lalacindyy.foodlab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by szeng on 4/15/18.
 */

public class RecipeManager extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppState state = AppState.getInstance();
        List<Dish> list = state.getDishes();
        Dish dish = list.get(state.getPosition());
        long recipeID = dish.getID();
        RecipeQuery query = new RecipeQuery(recipeID, getApplicationContext());
        query.setUrl();
        query.execute();
        finish();
    }
}
