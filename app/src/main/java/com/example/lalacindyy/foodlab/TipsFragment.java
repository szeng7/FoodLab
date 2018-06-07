package com.example.lalacindyy.foodlab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by szeng on 4/15/18.
 */

public class TipsFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private static final String TAG = "TipsFragment";
    private long recipeID;
    Recipe recipe;
    private Context context;

    private DatabaseReference tipsRef;
    List<TipItem> tipItemList;
    ListView listView;

    // newInstance constructor for creating fragment with arguments
    public static Fragment newInstance(int page, String title) {
        TipsFragment fragmentFirst = new TipsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        this.context = getContext();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        Button b = (Button) view.findViewById(R.id.button2);
        listView = view.findViewById(R.id.tipsListView);

        final AppState state = AppState.getInstance();
        recipe = state.getRecipe();
        recipeID = recipe.getID();
        Log.d("TIPSFRAGMENT", Long.toString(recipeID));
        tipsRef = FirebaseDatabase.getInstance().getReference("tips").child(Long.toString(recipeID));
        Toast.makeText(context, "Hold Your Tip to Edit", Toast.LENGTH_SHORT).show();

        /*updates listview whenever something is added*/

        tipsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tipItemList = new ArrayList<TipItem>();
                float averageRating = 0;
                int counter = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    float rating = child.child("rating").getValue(float.class);
                    String tip = child.child("tip").getValue(String.class);
                    String user = child.child("user").getValue(String.class);

                    TipItem x = new TipItem(user, tip, rating);
                    tipItemList.add(x);
                    averageRating += rating;
                    counter++;
                }

                Collections.reverse(tipItemList);
                TipsListViewAdapter adapter = new TipsListViewAdapter(context, R.layout.tip_item, tipItemList);
                listView.setAdapter(adapter);
                listView.setEmptyView(getView().findViewById(R.id.emptyElement));

                if (counter == 0) {
                    averageRating = 0;
                } else {
                    averageRating = averageRating / counter;
                }
                tipsRef = FirebaseDatabase.getInstance().getReference("ratings").child(Long.toString(recipeID));
                List<Float> list = new ArrayList();
                list.add(averageRating);
                tipsRef.setValue(list);
                Log.d("THIS IS THE AVERAGE", Float.toString(averageRating));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                MyCustomDialog popUpFragment = MyCustomDialog.newInstance((ArrayList<TipItem>) tipItemList);
                popUpFragment.show(fm, "pop_up_fragment");
            }
        });

        listView.setClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            SharedPreferences appData = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                FragmentManager fm = getFragmentManager();
                EditDialog edit = EditDialog.newInstance((ArrayList<TipItem>) tipItemList, pos);
                TipItem check = tipItemList.get(pos);
                if (appData.getString("name", "Anonymous").equals(check.getUser())) {
                    edit.show(fm, "pop_up_fragment");
                } else {
                    Toast.makeText(getActivity(), "This is not your tip", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Log.v("long clicked","pos: " + pos);

                return true;
            }
        });

        return view;
    }

}

