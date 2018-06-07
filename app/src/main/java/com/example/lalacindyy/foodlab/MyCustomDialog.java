package com.example.lalacindyy.foodlab;

import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

/**
 * Created by szeng on 4/24/18.
 */

public class MyCustomDialog extends DialogFragment{

    private DatabaseReference tipsRef;
    static private ArrayList<TipItem> tipItemList;

    public MyCustomDialog() {

    }

    public static MyCustomDialog newInstance(ArrayList<TipItem> list) {
        MyCustomDialog frag = new MyCustomDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        tipItemList = new ArrayList<TipItem>();
        tipItemList = list;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_dialog, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title", "Enter Tip");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button anonymous = view.findViewById(R.id.button4);
        Button enter = view.findViewById(R.id.button3);
        TextView name = view.findViewById(R.id.nameView);
        Button exit = view.findViewById(R.id.button5);

        SharedPreferences mpref = getActivity().getSharedPreferences("data", 0);
        String name_string = mpref.getString("name", "Anonymous");
        name.setText(name_string);

        anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String anon = "Anonymous";
                TextView name = getView().findViewById(R.id.nameView);
                name.setText(anon);
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText insertTips = getView().findViewById(R.id.tipView);
                String tipMessage = insertTips.getText().toString();
                if (tipMessage == null) {
                    tipMessage += " ";
                }
                TextView insertName = getView().findViewById(R.id.nameView);
                String name = insertName.getText().toString();

                RatingBar ratingBar = getView().findViewById(R.id.ratingBar);
                float rating = ratingBar.getRating();
                Log.d("DIALOG", Float.toString(rating));

                AppState state = AppState.getInstance();
                Recipe recipe = state.getRecipe();
                long recipeID = recipe.getID();

                tipsRef = FirebaseDatabase.getInstance().getReference("tips").child(Long.toString(recipeID));
                TipItem add = new TipItem(name, tipMessage, rating);
                tipItemList.add(add);
                tipsRef.setValue(tipItemList);
                Toast toast = Toast.makeText(getContext(), "Tip Entered!", Toast.LENGTH_SHORT);
                toast.show();
                dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
