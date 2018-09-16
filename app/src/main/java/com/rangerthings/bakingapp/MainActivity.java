package com.rangerthings.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rangerthings.bakingapp.Adapters.MainAdapter;
import com.rangerthings.bakingapp.DB.SharedViewModel;
import com.rangerthings.bakingapp.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel model;
    private MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //build the recyclerview adapter with an empty array
        adapter = new MainAdapter(this, new ArrayList<Recipe>(0));

        //Set up a view model: this will be a different instance than the detail activity's model
        //given the inability to delete/add entries in the recipe database this livedata observer is
        //unnecessary.  Just for practice.
        model = ViewModelProviders.of(this).get(SharedViewModel.class);
        model.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.updateRecipes(recipes);
            }
        });

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_main);

        //Check to see if device is tablet
        if (findViewById(R.id.frame_large) !=null){
            //The device is a tablet, set up grid view
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else{
            //The device is not a tablet
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

       /* adapter.setClickListener();*/
        recyclerView.setAdapter(adapter);


    }
}
