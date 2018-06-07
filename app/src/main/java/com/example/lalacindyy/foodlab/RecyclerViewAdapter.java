package com.example.lalacindyy.foodlab;

/**
 * Created by lalacindyy on 5/4/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;



/**
 * Created by User on 2/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImage = new ArrayList<>();
    private ArrayList<Boolean> myBoo = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<Integer> imageUrls, ArrayList<Boolean> boo) {
        mNames = names;
        mImage = imageUrls;
        myBoo = boo;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        int str = mImage.get(position);
        holder.image.setImageResource(str);
        holder.name.setText(mNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));
                //Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                if(myBoo.get(position)) {
                    myBoo.set(position, false);
                    holder.image.setBackgroundResource(0);
                } else {
                    myBoo.set(position, true);
                    holder.image.setBackgroundResource(R.layout.imagebutton_border);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView4);
            name = itemView.findViewById(R.id.rating);
        }
    }
}