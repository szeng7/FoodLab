package com.example.lalacindyy.foodlab;

/**
 * Created by szeng on 4/15/18.
 */

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;
    Boolean location;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items, Boolean location) {
        super(context, resourceId, items);
        this.context = context;
        this.location = location;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView timeNeeded;
        TextView rating;
        TextView ingredients;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.timeNeeded = (TextView) convertView.findViewById(R.id.time);
            holder.rating = (TextView) convertView.findViewById(R.id.rating);
            holder.ingredients = (TextView) convertView.findViewById(R.id.ingredients);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText("\n" + rowItem.getTitle());
        holder.timeNeeded.setText("Time Needed: " + rowItem.getTime() + " minutes");
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        if (location == false) {
            holder.rating.setText(df.format(rowItem.getRating()));
        }
        holder.ingredients.setText("Ingredients: " + rowItem.getIngredients());
        Picasso.with(getContext()).load(rowItem.getImageUrl()).into(holder.imageView);
        convertView.setBackground(getContext().getResources().getDrawable(R.drawable.list_item_border));
        return convertView;
    }
}
