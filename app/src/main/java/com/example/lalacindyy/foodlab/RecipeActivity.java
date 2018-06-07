package com.example.lalacindyy.foodlab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    private DrawerLayout mDrawerLayout;
    Dish dish;
    String name;
    String IMG;
    String TTL;
    long ID;
    MenuItem item;
    Boolean favorited;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //pref = this.getPreferences(MODE_PRIVATE);
        pref = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        name = pref.getString("name", "JD");

        edit = pref.edit();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
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

        // Locate the viewpager in activity_main.xml
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        // Set the ViewPagerAdapter into ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment(), "Ingredients");
        adapter.addFrag(new Fragment(), "Instructions");
        adapter.addFrag(new Fragment(), "Tips");

        viewPager.setAdapter(adapter);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.pager_header);
        mTabLayout.setupWithViewPager(viewPager);

        AppState state = AppState.getInstance();
        List<Dish> list = state.getDishes();
        dish = list.get(state.getPosition());
        String image = dish.getImage();
        String title = dish.getTitle();
        IMG = image;
        TTL = title;
        ID = dish.getID();

        ImageView pic = (ImageView) findViewById(R.id.imageView3);
        Picasso.with(this).load(image).into(pic);

        TextView text = (TextView) findViewById(R.id.textView9);
        text.setText(title);
        //edit.putBoolean("checked"+dish.getTitle(), false);
        //edit.commit();
        System.out.println("----------------------on create -------------------");

        ImageView home = this.findViewById(R.id.homebutton);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });

    }

    public void openDrawer(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Long id = dish.getID();
            DatabaseReference tipsRef = FirebaseDatabase.getInstance().getReference("ratings").child(Long.toString(id));
            tipsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        dish.setRating(child.getValue(float.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return IngredientsFragment.newInstance(0, "Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return InstructionsFragment.newInstance(1, "Page # 2");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return TipsFragment.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        item = menu.findItem(R.id.favorite_rec);
        SharedPreferences appData = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = appData.getString("name", "Anonymous");
        if (name.equals("Anonymous")) {
            Log.d("RecipeActivity", "black button");
            item.setIcon(R.drawable.ic_favorite_border_black_24dp);
            favorited = false;
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            return super.onPrepareOptionsMenu(menu);
        } else {
            Log.d("RecipeActivity", "maybe red button");
            DatabaseReference tipsRef = FirebaseDatabase.getInstance().getReference("myFav").child(name);
            tipsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean red = false;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (ID == child.child("id").getValue(Long.class)) {
                            item.setIcon(R.drawable.ic_favorite_red_24dp);
                            favorited = true;
                            Log.d("RecipeActivity", "red button");
                            red = true;
                            break;
                        }
                    }
                    if (red == false) {
                        item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                        favorited = false;
                        Log.d("RecipeActivity", "black button");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            return super.onPrepareOptionsMenu(menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //boolean isChecked = false;
        //noinspection SimplifiableIfStatement
        if (id == R.id.favorite_rec) {
            DatabaseReference cineIndustryRef = mDatabase.child("myFav").child(name);
            if(favorited == true){
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                Log.d("onOptionsSelected", "true");
                cineIndustryRef.child(Long.toString(ID)).removeValue();
                favorited = false;
            } else {
                //If the user hasn't already favorited this recipe, then fill in the heart
                //and add it to the favorites page
                favorited = true;
                Log.d("onOptionsSelected", "false");
                item.setIcon(R.drawable.ic_favorite_red_24dp);
                List<Dish> favItemList = new ArrayList<Dish>();
                Dish temp = dish;
                favItemList.add(temp);

                cineIndustryRef.child(Long.toString(ID)).setValue(temp);

                Toast.makeText(RecipeActivity.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.fav) {
            if (pref.getBoolean("signedIn", false)){
                Intent intent = new Intent(RecipeActivity.this, FavoritesActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(RecipeActivity.this, setting.class);
                startActivity(intent);
            }
        } else if (id == R.id.set) {
            if (pref.getBoolean("signedIn", false)){
                Intent intent = new Intent(RecipeActivity.this, setting_signIn.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(RecipeActivity.this, setting.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}