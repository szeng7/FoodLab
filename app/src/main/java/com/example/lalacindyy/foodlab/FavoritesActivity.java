package com.example.lalacindyy.foodlab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lalacindyy on 4/9/18.
 */

public class FavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    SharedPreferences appData;
    SharedPreferences.Editor appEditor;
    String imageUrl;
    String title;
    long id;
    float rating;
    int time;
    List<RowItem> list = new ArrayList<>();
    List<Dish> dishes = new ArrayList<>();
    private Context context;
    ListView recipelist;
    String ingredients;
    String name;
    CustomListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        context = getApplicationContext();


        Toast.makeText(getApplicationContext(), "Hold Recipe to Delete", Toast.LENGTH_LONG).show();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        appData = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        appEditor = appData.edit();

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

        name = appData.getString("name", "JD");
        DatabaseReference tipsRef = FirebaseDatabase.getInstance().getReference("myFav").child(name);
        tipsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    imageUrl = child.child("image").getValue(String.class);
                    title = child.child("title").getValue(String.class);
                    id = child.child("id").getValue(Long.class);
                    time = child.child("time").getValue(Integer.class);
                    rating = child.child("rating").getValue(Float.class);
                    ingredients = child.child("ingredients").getValue(String.class);
                    RowItem x = new RowItem(id, title, imageUrl, ingredients, rating, time);
                    Dish y = new Dish(id, title, imageUrl, rating);
                    list.add(x);
                    dishes.add(y);
                    Log.d("FavoritesActivity", title);
                }
                recipelist = findViewById(R.id.recipeListView);
                adapter = new CustomListViewAdapter(context,
                        R.layout.list_item, list, true);
                recipelist.setAdapter(adapter);
                AppState state = AppState.getInstance();
                state.setDishes(dishes);

                recipelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
                    {
                        Intent intent = new Intent(getApplicationContext(), RecipeManager.class);
                        AppState.getInstance().setPosition(position);
                        startActivity(intent);
                    }
                });

                recipelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    final CharSequence[] items = {"Delete"};
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int pos, long id) {
                        long iden = list.get(pos).getID();
                        DatabaseReference tipsRef = FirebaseDatabase.getInstance().getReference("myFav").child(name);
                        tipsRef.child(Long.toString(iden)).

                                removeValue();
                        list.remove(pos);
                        adapter.clear();
                        adapter.addAll(list);
                        adapter.notifyDataSetChanged();
                        return true;
                    }

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    public void openD(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(FavoritesActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.fav) {
            if (appData.getBoolean("signedIn", false)){
                Intent intent = new Intent(FavoritesActivity.this, FavoritesActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(FavoritesActivity.this, setting.class);
                startActivity(intent);
            }
        } else if (id == R.id.set) {
            if (appData.getBoolean("signedIn", false)){
                Intent intent = new Intent(FavoritesActivity.this, setting_signIn.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(FavoritesActivity.this, setting.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavoritesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        list.clear();
        adapter.clear();
        DatabaseReference tipsRef = FirebaseDatabase.getInstance().getReference("myFav").child(name);
        tipsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    imageUrl = child.child("image").getValue(String.class);
                    title = child.child("title").getValue(String.class);
                    id = child.child("id").getValue(Long.class);
                    time = child.child("time").getValue(Integer.class);
                    rating = child.child("rating").getValue(Float.class);
                    ingredients = child.child("ingredients").getValue(String.class);
                    RowItem x = new RowItem(id, title, imageUrl, ingredients, rating, time);
                    Dish y = new Dish(id, title, imageUrl, rating);
                    list.add(x);
                    dishes.add(y);
                    Log.d("FavoritesActivity", title);
                }
                recipelist = findViewById(R.id.recipeListView);
                adapter = new CustomListViewAdapter(context,
                        R.layout.list_item, list, true);
                recipelist.setAdapter(adapter);
                AppState state = AppState.getInstance();
                state.setDishes(dishes);

                recipelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
                    {
                        Intent intent = new Intent(getApplicationContext(), RecipeManager.class);
                        AppState.getInstance().setPosition(position);
                        startActivity(intent);
                    }
                });

                recipelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    final CharSequence[] items = {"Delete"};
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int pos, long id) {
                        long iden = list.get(pos).getID();
                        DatabaseReference tipsRef = FirebaseDatabase.getInstance().getReference("myFav").child(name);
                        tipsRef.child(Long.toString(iden)).

                                removeValue();
                        list.remove(pos);
                        adapter.clear();
                        adapter.addAll(list);
                        adapter.notifyDataSetChanged();
                        return true;
                    }

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

}
