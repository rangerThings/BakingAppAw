package com.rangerthings.bakingapp.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.rangerthings.bakingapp.Model.Recipe;

import java.util.List;


@Dao
public interface RecipeDao {


        //insert a single recipeResults
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Recipe recipeResults);

        //delete a single recipe with the in Id
        @Query("DELETE FROM recipe_table WHERE id = :inId")
        void deleteRecipe(Integer inId);

        //return all of the movies
        @Query("SELECT * from recipe_table ORDER BY name ASC")
        LiveData<List<Recipe>> getAllRecipes();


      /*  //return all of the movies
        @Query("SELECT * from recipe_table ORDER BY name ASC")
        List<Recipe> getAllRecipes();*/
// --Commented out by Inspection START (8/4/2018 10:02 PM):
//    //delete all of the movies in the table
//    @Query("DELETE FROM movie_table")
//    void killTable();
// --Commented out by Inspection STOP (8/4/2018 10:02 PM)

        //get a specific movie
        @Query("SELECT * from recipe_table WHERE id = :inId")
        Recipe getSingleRecipe(Integer inId);

    }

