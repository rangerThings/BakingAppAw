package com.rangerthings.bakingapp.Fragments;


import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.rangerthings.bakingapp.Adapters.StepsAdapter;
import com.rangerthings.bakingapp.DB.SharedViewModel;
import com.rangerthings.bakingapp.Model.Ingredient;
import com.rangerthings.bakingapp.Model.Recipe;
import com.rangerthings.bakingapp.Model.Step;
import com.rangerthings.bakingapp.R;
import com.rangerthings.bakingapp.Widget.IngredientsAppWidget;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class StepsFragment extends Fragment {

    private SharedViewModel model;
    private List<Step> currentSteps;
    private TextView tvIngredients;
    private String recipeName;
    private List<Ingredient> ingredientsList;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get reference to activity viewmodel (same instance as initial detail activity)
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);


        //grab the current recipeID from the viewmodel (set in the StepDetailActivity)
        Integer currentRecipeId = model.getCurrentRecipeId();
        try {
            //try to query the database for a recipe with that current ID
            Recipe currentRecipe = model.getSingleRecipe(currentRecipeId);

            //get current recipe name
            recipeName = currentRecipe.getName();

            //get the current ingredient list
            ingredientsList = currentRecipe.getIngredients();
            //feed that ingredient list into a string builder
            buildIngredients();

            //get the steps from that recipe
            currentSteps = currentRecipe.getSteps();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        tvIngredients = view.findViewById(R.id.tv_ingredientList);
        TextView tvRecipeName = view.findViewById(R.id.tv_recipeName);
        TextView tvIngredientTitle = view.findViewById(R.id.tv_ingredientTitle);


        //set ingredients as gone/collapsed
        tvIngredients.setVisibility(View.GONE);

        //Set click listener on each view
        tvIngredientTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMethod(v);
            }
        });
        tvIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMethod(v);
            }
        });

        //Set the textview with the string
        String ingredients = model.getIngredientString();
        tvIngredients.setText(ingredients);
        tvRecipeName.setText(recipeName);

        //send the updated ingredients list to the app widget
        //code modified from https://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
        RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.ingredients_app_widget);
        ComponentName thisWidget = new ComponentName(getActivity(), IngredientsAppWidget.class);
        remoteViews.setTextViewText(R.id.appwidget_text, ingredients);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

        //Build a recyclerview using the currentSteps pulled from the currentRecipe in the viewmodel
        RecyclerView rv = view.findViewById(R.id.rv_steps);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        //pass in context, current steps for the recipe, and the model(for data passing)
        rv.setAdapter(new StepsAdapter(this.getActivity(), currentSteps, model));


        return view;
    }

    private void onClickMethod(View v) {
        if (tvIngredients.getVisibility() == View.VISIBLE) {
            //view is visible, hide it
            tvIngredients.setVisibility(View.GONE);
        } else {
            tvIngredients.setVisibility(View.VISIBLE);
        }
    }


    public StepsFragment() {
        // Required empty public constructor
    }


    public static StepsFragment newInstance() {
        return new StepsFragment();
    }

    private void buildIngredients() {
        //set the ingredient list
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredients : ingredientsList) {
            String ingredientName = ingredients.getIngredient();
            Float ingredientQuant = ingredients.getQuantity();
            String ingredientMeasure = ingredients.getMeasure();
            builder.append("-" + ingredientName.toUpperCase() + " (" + ingredientQuant + " " + ingredientMeasure + ")" + "\n");
        }
        model.setIngredientString(builder.toString());

    }

}
