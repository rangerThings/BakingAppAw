package com.rangerthings.bakingapp.DB;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rangerthings.bakingapp.Model.Step;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


//https://medium.com/@toddcookevt/android-room-storing-lists-of-objects-766cca57e3f9
public class StepConverter {

        private final Gson gson = new Gson();

        @TypeConverter
        public List<Step> stringToStepList(String data) {
            if (data == null) {
                return Collections.emptyList();
            }

            Type listType = new TypeToken<List<Step>>() {}.getType();

            return gson.fromJson(data, listType);
        }

        @TypeConverter
        public String stepListToString(List<Step> ingredientObjects) {
            return gson.toJson(ingredientObjects);
        }
    }
