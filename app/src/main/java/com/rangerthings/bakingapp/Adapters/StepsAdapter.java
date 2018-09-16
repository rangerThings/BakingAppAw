package com.rangerthings.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rangerthings.bakingapp.Fragments.DetailFragment;
import com.rangerthings.bakingapp.Model.Step;
import com.rangerthings.bakingapp.R;
import com.rangerthings.bakingapp.DB.SharedViewModel;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder>{
    private final SharedViewModel mModel;
    private List<Step> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;


    //Simplified adapter modified from https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
    // data is passed into the constructor
    public StepsAdapter(Context context, List<Step> data, SharedViewModel model) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        this.mModel = model;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_item_steps, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String stepDescription = mData.get(position).getShortDescription();
        if(mModel.getStepId() == position){
            holder.myTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        }else {
            holder.myTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
        }
        holder.myTextView.setText(stepDescription);
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
            myTextView = itemView.findViewById(R.id.tv_steps);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //fire the on click method
            onItemClick(view, getAdapterPosition());
        }
    }

    private void onItemClick(View view, int position){

        int clickedTextViewPos = position;
        Log.d("Steps", String.valueOf(clickedTextViewPos));

        //set the current step in the viewmodel
        mModel.setCurrentStep(mData.get(position));
        mModel.setStepId(position);


        if (mModel.getIsTablet()){
            //device is a tablet
            DetailFragment detailFragment = DetailFragment.newInstance();
            ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_two, detailFragment).commit();

        }else{
            //device is not a tablet
            //Launch the Detail Fragment and have it replace the steps fragment
            DetailFragment detailFragment = DetailFragment.newInstance();
            ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).commit();

        }
        notifyDataSetChanged();
        }


    public void updateRecipes(List<Step> recipe) {
        mData = recipe;
        notifyDataSetChanged();
    }
}
