package com.example.lalacindyy.foodlab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

/**
 * Created by szeng on 4/23/18.
 */

public class TipsListViewAdapter extends ArrayAdapter<TipItem> {

    Context context;
    List<TipItem> tipsList;

    public TipsListViewAdapter(Context context, int resourceId,
                                     List<TipItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.tipsList = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.tip_item, null, true);

        TipItem tipItem = tipsList.get(position);

        TextView tip = (TextView) listView.findViewById(R.id.textView10);
        TextView name = (TextView) listView.findViewById(R.id.textView11);
        RatingBar rating = (RatingBar) listView.findViewById(R.id.ratingBar2);
        TextView ratingNumber = (TextView) listView.findViewById(R.id.ratingNumber);

        tip.setText(tipItem.getTip());
        name.setText(tipItem.getUser());
        ratingNumber.setText(String.format(Locale.US, "%.1f", tipItem.getRating()));
        float number = tipItem.getRating();
        rating.setRating(number);
        Drawable drawable = rating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

        return listView;
    }


}
