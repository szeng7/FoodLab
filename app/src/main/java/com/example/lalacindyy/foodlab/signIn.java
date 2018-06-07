package com.example.lalacindyy.foodlab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lalacindyy on 4/9/18.
 */

public class signIn extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private DatabaseReference mDatabase;

    SharedPreferences appData;
    SharedPreferences.Editor appEditor;
    EditText name;
    EditText password;
    EditText email;
    String email_test;
    Boolean exist;
    Boolean exist_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        email = findViewById(R.id.editText3);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        appData = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        appEditor = appData.edit();

        if (appData.getBoolean("signedIn", false)) {
            name.setText(appData.getString("name", "JD"));
            password.setText(appData.getString("password", "01"));
            email.setText(appData.getString("email", "myEmail"));
        }

        if (appData.getBoolean("signedIn", false)) {
            View hView = navigationView.inflateHeaderView(R.layout.nev_header_signin);
            TextView tv = (TextView) hView.findViewById(R.id.textView2);
            tv.setText(appData.getString("name", "JD"));
            TextView text = (TextView) hView.findViewById(R.id.textView3);
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
            Intent intent = new Intent(signIn.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.fav) {
            if (appData.getBoolean("signedIn", false)) {
                Intent intent = new Intent(signIn.this, FavoritesActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(signIn.this, setting.class);
                startActivity(intent);
            }
        } else if (id == R.id.set) {
            if (appData.getBoolean("signedIn", false)) {
                Intent intent = new Intent(signIn.this, setting_signIn.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(signIn.this, setting.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeData(View view) {
        //sign in
        final String Password = password.getText().toString();
        final String Email = email.getText().toString();
        exist_2 = false;

        if(Password.matches("")|| Email.matches("")){
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            return;
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String temp = snapshot.child("email").getValue().toString();
                    if (temp.equals(Email)) {
                        String pw = snapshot.child("password").getValue().toString();
                        String name = snapshot.child("username").getValue().toString();
                        if (pw.equals(Password)) {
                            exist_2 = true;
                            appEditor.putBoolean("signedIn", true);
                            appEditor.putString("name", name);
                            appEditor.putString("password", Password);
                            appEditor.putString("email", Email);

                            appEditor.commit();
                            finish();
                            Intent intent = new Intent(signIn.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(signIn.this, "Wrong Password, try again!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (!exist_2) {
                    Toast.makeText(signIn.this, "Unable to find email, try again!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(signIn.this, "Sign in success!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        DatabaseReference mRef = mDatabase.child("Users");
        mRef.addValueEventListener(postListener);
    }
}

