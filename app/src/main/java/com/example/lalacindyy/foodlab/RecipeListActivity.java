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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    String name;
    private DrawerLayout mDrawerLayout;
    List<Dish> ingredientDishes = new ArrayList<Dish>();
    List<Dish> dishes = new ArrayList<Dish>();
    List<Dish> timeDishes = new ArrayList<Dish>();
    List<Dish> ratingDishes = new ArrayList<Dish>();
    List<RowItem> listToDisplay = new ArrayList<RowItem>();
    AppState state;
    private final String TAG = "Main Activity";
    ListView listView;
    float averageRating;
    CustomListViewAdapter adapter;
    private int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //pref = this.getPreferences(MODE_PRIVATE);
        pref = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        name = pref.getString("name", "JD");

        edit = pref.edit();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView backButton = (ImageView) findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });

        if(pref.getBoolean("signedIn", false)){
            View hView =  navigationView.inflateHeaderView(R.layout.nev_header_signin);
            TextView tv = (TextView)hView.findViewById(R.id.textView2);
            tv.setText(pref.getString("name", "JD"));
            TextView text = (TextView)hView.findViewById(R.id.textView3);
            text.setText(pref.getString("email", "myemail"));

        } else {
            navigationView.inflateHeaderView(R.layout.nav_header);
        }

        navigationView.setNavigationItemSelectedListener(this);

        Log.i(TAG, "Create list from input");

        state = AppState.getInstance();
        dishes = state.getDishes();

        if (dishes.size() == 0) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "Unknown ingredient, please enter another", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView home = this.findViewById(R.id.homebutton);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });

        Boolean addToEnd = true;

        ingredientDishes = dishes;

        //sort by rating and time needed
        timeDishes.add(dishes.get(0));
        for (int i = 1; i < dishes.size(); i++) {
            for (int j = 0; j < timeDishes.size(); j++) {
                addToEnd = true;
                if (dishes.get(i).getTime() < timeDishes.get(j).getTime()) {
                    timeDishes.add(j, dishes.get(i));
                    addToEnd = false;
                    break;
                }
            }
            if (addToEnd == true) {
                timeDishes.add(dishes.get(i));
            }
        }

        ratingDishes.add(dishes.get(0));
        for (int i = 1; i < dishes.size(); i++) {
            for (int j = 0; j < ratingDishes.size(); j++) {
                addToEnd = true;
                if (dishes.get(i).getRating() > ratingDishes.get(j).getRating()) {
                    ratingDishes.add(j, dishes.get(i));
                    addToEnd = false;
                    break;
                }
            }
            if (addToEnd == true) {
                ratingDishes.add(dishes.get(i));
            }
        }

        for (int i = 0; i < (dishes.size()); ++i) {
            Log.d(TAG, Integer.toString(i));
            RowItem item = new RowItem(dishes.get(i).getID(), dishes.get(i).getTitle(), dishes.get(i).getImage(), dishes.get(i).getIngredients(), dishes.get(i).getRating(), dishes.get(i).getTime());
            listToDisplay.add(item);
        }

        listView = (ListView)findViewById(R.id.recipeListView);
        adapter = new CustomListViewAdapter(this,
                R.layout.list_item, listToDisplay, false);

        listView.setAdapter(adapter);

        Spinner sortOption = this.findViewById(R.id.sort);
        sortOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinnerPosition = position;
                if (position == 0) { //sort by ingredient
                    listToDisplay.clear();
                    adapter.clear();
                    for (int i = 0; i < (ingredientDishes.size()); ++i) {
                        RowItem item = new RowItem(ingredientDishes.get(i).getID(), ingredientDishes.get(i).getTitle(), ingredientDishes.get(i).getImage(), ingredientDishes.get(i).getIngredients(), ingredientDishes.get(i).getRating(), ingredientDishes.get(i).getTime());
                        adapter.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    state.setDishes(ingredientDishes);

                } else if (position == 1) { //sort by time
                    listToDisplay.clear();
                    adapter.clear();
                    for (int i = 0; i < (timeDishes.size()); ++i) {
                        RowItem item = new RowItem(timeDishes.get(i).getID(), timeDishes.get(i).getTitle(), timeDishes.get(i).getImage(), timeDishes.get(i).getIngredients(), timeDishes.get(i).getRating(), timeDishes.get(i).getTime());
                        adapter.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    state.setDishes(timeDishes);
                } else { //sort by rating
                    listToDisplay.clear();
                    adapter.clear();
                    for (int i = 0; i < (ratingDishes.size()); ++i) {
                        RowItem item = new RowItem(ratingDishes.get(i).getID(), ratingDishes.get(i).getTitle(), ratingDishes.get(i).getImage(), ratingDishes.get(i).getIngredients(), ratingDishes.get(i).getRating(), ratingDishes.get(i).getTime());
                        adapter.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    state.setDishes(ratingDishes);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });




        // Set the onclick function of each item to go to the quiz activity while passing in
        // selection data
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i(TAG, "List View on Click Pressed. Loading description for " + position);
                Intent intent = new Intent(getApplicationContext(), RecipeManager.class);
                AppState.getInstance().setPosition(position);
                startActivity(intent);
            }
        });


    }

    public void openDrawer(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        ratingDishes.clear();
        Boolean addToEnd = true;
        ratingDishes.add(dishes.get(0));
        for (int i = 1; i < dishes.size(); i++) {
            for (int j = 0; j < ratingDishes.size(); j++) {
                addToEnd = true;
                if (dishes.get(i).getRating() > ratingDishes.get(j).getRating()) {
                    ratingDishes.add(j, dishes.get(i));
                    addToEnd = false;
                    break;
                }
            }
            if (addToEnd == true) {
                ratingDishes.add(dishes.get(i));
            }
        }

        Log.d("spinnerNum", Integer.toString(spinnerPosition));
        if (spinnerPosition == 2) {
            dishes = ratingDishes;
        }
        listToDisplay.clear();
        adapter.clear();
        for (int i = 0; i < dishes.size(); i++) {
            RowItem item = new RowItem(dishes.get(i).getID(), dishes.get(i).getTitle(), dishes.get(i).getImage(), dishes.get(i).getIngredients(), dishes.get(i).getRating(), dishes.get(i).getTime());
            adapter.add(item);
        }
        adapter.notifyDataSetChanged();
        state.setDishes(dishes);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(RecipeListActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.fav) {
            if (pref.getBoolean("signedIn", false)){
                Intent intent = new Intent(RecipeListActivity.this, FavoritesActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(RecipeListActivity.this, setting.class);
                startActivity(intent);
            }
        } else if (id == R.id.set) {
            if (pref.getBoolean("signedIn", false)){
                Intent intent = new Intent(RecipeListActivity.this, setting_signIn.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(RecipeListActivity.this, setting.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
