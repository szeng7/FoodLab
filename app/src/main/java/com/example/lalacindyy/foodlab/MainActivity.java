package com.example.lalacindyy.foodlab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private DrawerLayout mDrawerLayout;
    SharedPreferences appData;
    SharedPreferences.Editor appEditor;
    TextView ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        appData = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        appEditor = appData.edit();

        ingredients = findViewById(R.id.autoCompleteTextView);

        if(appData.getBoolean("signedIn", false)){
            View hView =  navigationView.inflateHeaderView(R.layout.nev_header_signin);
            TextView tv = (TextView)hView.findViewById(R.id.textView2);
            tv.setText(appData.getString("name", "JD"));
            TextView text = (TextView)hView.findViewById(R.id.textView3);
            text.setText(appData.getString("email", "myemail"));

        } else {
            navigationView.inflateHeaderView(R.layout.nav_header);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void openDrawer(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.fav) {
            if (appData.getBoolean("signedIn", false)){
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, setting.class);
                startActivity(intent);
            }
        } else if (id == R.id.set) {
            if (appData.getBoolean("signedIn", false)){
                Intent intent = new Intent(MainActivity.this, setting_signIn.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, setting.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void searchRecipe(View view) {
        String ingredientString = ingredients.getText().toString().trim().replace(' ', '-');
        if (ingredientString != "") {
            ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading your Recipes!");
            pd.show();
            pd.setCancelable(true);
            AppState state = AppState.getInstance();
            state.setingredients(ingredientString);
            final String[] recipeList = state.getIngredients().split(",");
            Log.d("Constructor", recipeList[0]);
            JSONQuery query = new JSONQuery(recipeList, getApplicationContext());
            query.setUrl();
            query.execute();
        } else {
            Toast.makeText(this,
                    "Please enter some ingredients",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
