package com.example.lalacindyy.foodlab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by lalacindyy on 4/13/18.
 */

public class setting_signIn extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private DatabaseReference mDatabase;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImage = new ArrayList<>();
    private ArrayList<Boolean> myBoo = new ArrayList<>();
    SharedPreferences appData;
    SharedPreferences.Editor appEditor;
    //EditText name;
    EditText password;
    EditText password2;
    EditText email;
    TextView Name;
    Boolean exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_signin);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDatabase = FirebaseDatabase.getInstance().getReference();



        //add data to recycler view
        setData();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.res_option, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //name = findViewById(R.id.editText);
        //Name = findViewById(R.id.textView6);
        password = findViewById(R.id.editText2);
        password2 = findViewById(R.id.editText4);
        email = findViewById(R.id.editText3);
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

    }

    public void openD(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(setting_signIn.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.fav) {
            if (appData.getBoolean("signedIn", false)){
                Intent intent = new Intent(setting_signIn.this, FavoritesActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(setting_signIn.this, setting.class);
                startActivity(intent);
            }
        } else if (id == R.id.set) {
            if (appData.getBoolean("signedIn", false)){
                Intent intent = new Intent(setting_signIn.this, setting_signIn.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(setting_signIn.this, setting.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeData(View view) {
        //need to update the firebase
        appEditor.clear();
        appEditor.putBoolean("signedIn", false);
        appEditor.commit();
        Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(setting_signIn.this, setting.class);
        startActivity(intent);
    }

    public void saveData(View view) {
        final String userName = appData.getString("name", "JD");
        final String Password = password.getText().toString();
        final String Password2 = password2.getText().toString();
        final String Email = email.getText().toString();
        exist = false;

        if(Password.matches("")&& Email.matches("")){
            Toast.makeText(this, "Invalid username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Password.matches("")) {
            if(!Password.equals(Password2)) {
                Toast.makeText(this, "password doesn't match", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mDatabase.child("Users").child(userName).child("password").setValue(Password);
                appEditor.putString("password", Password);

                appEditor.commit();
                Toast.makeText(setting_signIn.this, "password changed!", Toast.LENGTH_SHORT).show();
                password.setText("");
                password2.setText("");
//                Intent intent = new Intent(setting_signIn.this, MainActivity.class);
//                startActivity(intent);
            }
        }


        if (!Email.matches("")) {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    if (!exist) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (i == 0) {
                                //System.out.println("------inside for loop");
                                i = 1;
                            }
                            String temp = snapshot.child("email").getValue().toString();
                            if (temp.equals(Email)) {
                                //System.out.println("---------within first loop");
                                Toast.makeText(setting_signIn.this, "email already exist!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                    exist = true;

                    mDatabase.child("Users").child(userName).child("email").setValue(Email);
                    //mDatabase.child("Users").child(userName).child("password").setValue(Password);

                    //System.out.println("---------adding name");
                    appEditor.putBoolean("signedIn", true);
                    //appEditor.putString("password", Password);
                    appEditor.putString("email", Email);

                    appEditor.commit();

                    Toast.makeText(setting_signIn.this, "Email changed!", Toast.LENGTH_SHORT).show();
                    email.setText("");
//                    Intent intent = new Intent(setting_signIn.this, MainActivity.class);
//                    startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            DatabaseReference mRef = mDatabase.child("Users");
            mRef.addValueEventListener(postListener);
        }


    }

    public void setData() {
        mImage.add(R.drawable.fast);
        myBoo.add(false);
        mNames.add("Fast Food");

        mImage.add(R.drawable.indian);
        myBoo.add(false);
        mNames.add("Indian");

        mImage.add(R.drawable.japan);
        myBoo.add(false);
        mNames.add("Japanese");

        mImage.add(R.drawable.thai);
        myBoo.add(false);
        mNames.add("Thai Food");

        mImage.add(R.drawable.taco);
        myBoo.add(false);
        mNames.add("Mexican");

        mImage.add(R.drawable.rice);
        myBoo.add(false);
        mNames.add("Chinese");

        mImage.add(R.drawable.pizza);
        myBoo.add(false);
        mNames.add("Italian");

        initRecyclerView();
    }

    private void initRecyclerView(){
        //Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImage, myBoo);
        recyclerView.setAdapter(adapter);
    }


}
