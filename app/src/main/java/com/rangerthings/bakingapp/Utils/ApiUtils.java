package com.rangerthings.bakingapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rangerthings.bakingapp.Model.Recipe;
import com.rangerthings.bakingapp.DB.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class ApiUtils {

    private static Retrofit retrofit = null;

    //Singleton pattern for retrofit client, create instance if one doesn't exists
    //Hooked by getMovieService
    private static Retrofit getClient(String baseUrl) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    //Returns movie service with defined interface
    private static ApiInterface getApiService() {
        return getClient(Constants.BASE_URL).create(ApiInterface.class);

    }

    //The request to Movie API services
    interface ApiInterface
    {

        //Only call to API for baking app
        @GET("baking.json")
        Call <List<Recipe>> getRecipeData();

    }

    //BLOCK OF ASYNC METHODS
    //Redundant, but probably not worth building a generic method for

    //Actual async call to get results from the movie api or local database
    public static void loadRecipes(final RecipeRepository mRepository) {
        ApiInterface mService = getApiService();
            mService.getRecipeData().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                    if (response.isSuccessful()) {
                        Log.d("RecipeResults API Util", "posts loaded from API");
                        ArrayList<Recipe> recipeResults = new ArrayList<>();

                        recipeResults.addAll(response.body());
                        mRepository.bulkInsert(recipeResults);

                    } else {
                        int statusCode = response.code();
                        Log.d("Failed Status Code", String.valueOf(statusCode));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                    Log.d("MainActivity", "error loading from API");
                }

            });
        }
    //checks network connection, does not specifically see if there is active internet (no ping)
    //Simplest answer from https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    //Slightly modified

    public static Boolean isOnline(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    }

