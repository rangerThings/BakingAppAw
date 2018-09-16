package com.rangerthings.bakingapp.DB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.rangerthings.bakingapp.DB.RecipeRepository;
import com.rangerthings.bakingapp.Model.Recipe;
import com.rangerthings.bakingapp.Model.Step;

import java.util.List;
import java.util.concurrent.ExecutionException;


import static android.content.ContentValues.TAG;

public class SharedViewModel extends AndroidViewModel {

    private boolean isTablet = false;
    private Integer currentRecipeId;
    private final RecipeRepository mRepository;
    private final LiveData<List<Recipe>> mAllRecipes;
    private Step currentStep;
    private String currentOrientation;
    private String mIngredientString;
    private int stepId;


    public SharedViewModel(Application application) {
        super(application);
        mRepository = new RecipeRepository(application);

        Log.d(TAG, "Receiving data from database");
       mAllRecipes = mRepository.getmAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }


    public Recipe getSingleRecipe(Integer inId) throws ExecutionException, InterruptedException {
        return mRepository.getSingleRecipe(inId);
    }

    public void insert(Recipe recipeResults) {
        mRepository.insertMovie(recipeResults);
    }
    public void deleteMovie(Integer inId) {
        mRepository.deleteRecipe(inId);
    }

    //UI
    public void setCurrentRecipeId(Integer recipeId){
        currentRecipeId = recipeId;
    }

    public Integer getCurrentRecipeId(){
        return currentRecipeId;
    }

    public void setCurrentStep(Step step){ currentStep=step;}
    public Step getCurrentStep(){return currentStep;}

    public void setStepId(int inStep){stepId = inStep;}
    public int getStepId(){return stepId;}


    public void setOrientation (String orientation){currentOrientation = orientation;}
    public String getOrientation(){return currentOrientation;}

    public void setIsTablet(boolean tablet){isTablet = tablet;}
    public boolean getIsTablet(){return isTablet;}

    public void setIngredientString(String ingredientString){mIngredientString = ingredientString;}
    public String getIngredientString(){return mIngredientString;}

}
