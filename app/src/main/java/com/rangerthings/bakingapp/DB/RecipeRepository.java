package com.rangerthings.bakingapp.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.rangerthings.bakingapp.Model.Recipe;
import com.rangerthings.bakingapp.Utils.ApiUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class RecipeRepository {

    private final RecipeDao mRecipeDao;
    private final LiveData<List<Recipe>> mAllRecipes;


    public RecipeRepository(Application application) {
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        mRecipeDao = db.movieDao();
        mAllRecipes = mRecipeDao.getAllRecipes();


        //query API and insert recipe into database if the device is online
        if (ApiUtils.isOnline(application.getApplicationContext())) {
            ApiUtils.loadRecipes(this);
        }
    }


    //    //bulk inserts all movies in the api list
    public void bulkInsert(List<Recipe> recipeResults) {
        for (Recipe recipe : recipeResults) {

            insertMovie(recipe);
        }
    }


    //    //deletes all entries
    public void killTable() {
        new deleteAsyncTask(mRecipeDao).execute();
    }

    //delete single movie
    public void deleteRecipe(Integer inId) {
        new deleteAsyncTask(mRecipeDao).execute(inId);
    }

    //get movies from the database
    public LiveData<List<Recipe>> getmAllRecipes() {
        return mAllRecipes;
    }

    //checks to see if idExists
    public Recipe getSingleRecipe(Integer inId) throws ExecutionException, InterruptedException {
        return new queryAsyncTask(mRecipeDao).execute(inId).get();
    }

    //insert single movie into database
    public void insertMovie(Recipe recipeResults) {
        new insertAsyncTask(mRecipeDao).execute(recipeResults);
    }


    //use async to retrieve single recipe from database, won't block ui thread
    private static class queryAsyncTask extends AsyncTask<Integer, Void, Recipe> {

        private final RecipeDao mAsyncTaskDao;

        queryAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Recipe doInBackground(final Integer... params) {
            return mAsyncTaskDao.getSingleRecipe(params[0]);
        }
    }

    //call to API


    //use async to insert movie into database, won't block ui thread
    private static class insertAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private final RecipeDao mAsyncTaskDao;

        insertAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //use async to delete movie into database, won't block ui thread
    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private final RecipeDao mAsyncTaskDao;

        deleteAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.deleteRecipe(params[0]);
            return null;
        }
    }

}

