package com.rangerthings.bakingapp.DB;

//From Google "Room With a View" codelab
//Room is a database layer on top of an SQLite database. Room takes care of mundane tasks
//that you used to handle with an SQLiteOpenHelper.


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.rangerthings.bakingapp.Model.Recipe;


@Database(entities = {Recipe.class}, version =1)
public abstract class RecipeRoomDatabase extends RoomDatabase {

    //create the dao
    public abstract RecipeDao movieDao();

    //get an instance of the database if it exists, or create it
    private static RecipeRoomDatabase INSTANCE;

    //Singleton so only one instance of database exists
    public static RecipeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeRoomDatabase.class, "movie_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
