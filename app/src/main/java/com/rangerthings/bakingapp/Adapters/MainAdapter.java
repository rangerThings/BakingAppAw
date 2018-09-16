package com.rangerthings.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rangerthings.bakingapp.Model.Recipe;
import com.rangerthings.bakingapp.R;
import com.rangerthings.bakingapp.StepDetailActivity;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private List<Recipe> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;

    //Simplified adapter modified from https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
    // data is passed into the constructor
    public MainAdapter(Context context, List<Recipe> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_item_main, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String recipeName = mData.get(position).getName();
        holder.myTextView.setText(recipeName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //fire the on click method
            onItemClick(view, getAdapterPosition());
        }
    }

    private void onItemClick(View view, int position){

        //Launch the Detail Activity
        //get the current recipe id to pass to intent, seems better than full object
        Intent detailIntent = new Intent(mContext, StepDetailActivity.class);
        detailIntent.putExtra("currentRecipeId", currentRecipeId(position));
        Log.d("Current Id:", String.valueOf(currentRecipeId(position)));
        mContext.startActivity(detailIntent);

    }

    private Integer currentRecipeId(int position){
        return mData.get(position).getId();
    }

    public void updateRecipes(List<Recipe> recipe) {
        mData = recipe;
        notifyDataSetChanged();
    }
}
