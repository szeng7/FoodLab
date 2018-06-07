package com.example.lalacindyy.foodlab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Created by szeng on 4/15/18.
 */

public class InstructionsFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static Fragment newInstance(int page, String title) {
        InstructionsFragment fragmentFirst = new InstructionsFragment();
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
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instructions, container, false);
        AppState state = AppState.getInstance();
        try {
            Recipe recipe = state.makeRecipe();
            String[] instructions = recipe.getSteps();
            ListView listView = (ListView) view.findViewById(R.id.instructionsView);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    instructions);

            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
