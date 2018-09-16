package com.rangerthings.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.rangerthings.bakingapp.DB.SharedViewModel;
import com.rangerthings.bakingapp.Fragments.DetailFragment;
import com.rangerthings.bakingapp.Fragments.StepsFragment;
import com.rangerthings.bakingapp.Model.Recipe;
import com.rangerthings.bakingapp.Model.Step;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StepDetailActivity extends AppCompatActivity {

    private SharedViewModel model;
    private Integer stepId;
    private List<Step> listSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //get a new instance of the viewmodel
        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        //check the current orientation, and set it in model
        int currentOrientation = getResources().getConfiguration().orientation;
        setCurrentOrientation(currentOrientation);

        //get the current recipe id from the intent in main adapter
        //will use the sharedview model to pass information between fragments/activities
        Integer currentRecipeId = getIntent().getIntExtra("currentRecipeId", 1);

        //Try to get that recipe from the database
        try {
            model.setCurrentRecipeId(currentRecipeId);
            Recipe currentRecipe = model.getSingleRecipe(currentRecipeId);
            listSteps = currentRecipe.getSteps();

            //Set first step as 'filler' step in tablet mode, replaced on click in step adapter
            model.setCurrentStep(listSteps.get(0));


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
//Check to see if device is tablet, set as true in view model
        if (findViewById(R.id.linearLayout_large) != null) {
            model.setIsTablet(true);
        }


        //if the model is not a tablet inflate bottom navigation and set listener
        if (!model.getIsTablet()) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener
                    (new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            //uses step id to determine how to navigate
                            boolean homePressed = false;
                            stepId = model.getCurrentStep().getId();
                            switch (item.getItemId()) {
                                case R.id.action_previous:
                                    if (stepId > 0) {
                                        model.setCurrentStep(listSteps.get(stepId - 1));
                                        model.setStepId(model.getCurrentStep().getId());
                                    } else {
                                        //Step is 0,, go to last step
                                        model.setCurrentStep(listSteps.get(listSteps.size() - 1));
                                        model.setStepId(model.getCurrentStep().getId());
                                    }
                                    break;
                                case R.id.action_recipes: //
                                    // go home
                                    homePressed = true;
                                    break;
                                case R.id.action_next:
                                    if (stepId < (listSteps.size() - 1)) {
                                        model.setCurrentStep(listSteps.get(stepId + 1));
                                        model.setStepId(model.getCurrentStep().getId());
                                    } else {
                                        //equal to or greater than total list
                                        //back to zero
                                        model.setCurrentStep(listSteps.get(0));
                                        model.setStepId(model.getCurrentStep().getId());
                                    }
                                    break;
                            }
                            if (homePressed) {
                                //user pressed the home button
                                StepsFragment stepsFragment = StepsFragment.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, stepsFragment).commit();

                            } else {
                                //user did not press home button
                                DetailFragment detailFragment = DetailFragment.newInstance();
                                if (model.getIsTablet()) {
                                    //it is a tablet
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_two, detailFragment).commit();
                                } else {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).commit();

                                }
                            }
                            return true;
                        }
                    });}


            //if there is no existing fragment
            if (savedInstanceState == null) {
                if (model.getIsTablet()) {
                    //device is a tablet
                    StepsFragment stepsFragment = StepsFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, stepsFragment).commit();
                    DetailFragment detailFragment = DetailFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_two, detailFragment).commit();

                } else {
                    //device is not a tablet
                    //call the steps fragment
                    StepsFragment stepsFragment = StepsFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, stepsFragment).commit();
                }

            }
        }

        private void setCurrentOrientation ( int currentOrientation){

            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Landscape
                model.setOrientation("landscape");
            } else {
                // Portrait
                model.setOrientation("portrait");
            }
        }

    }

