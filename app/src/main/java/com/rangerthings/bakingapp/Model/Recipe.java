package com.rangerthings.bakingapp.Model;

import java.util.List;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rangerthings.bakingapp.DB.IngredientConverter;
import com.rangerthings.bakingapp.DB.StepConverter;

@Entity(tableName = "recipe_table")
public class Recipe implements Parcelable{

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    private Integer id;


    @SerializedName("name")
    @ColumnInfo(name = "name")
    @Expose
    private String name;


    @TypeConverters(IngredientConverter.class)
    @SerializedName("ingredients")
    @ColumnInfo(name = "ingredients")
    @Expose
    private List<Ingredient> ingredients = null;


    @TypeConverters(StepConverter.class)
    @SerializedName("steps")
    @ColumnInfo(name = "steps")
    @Expose
    private List<Step> steps = null;


    @SerializedName("servings")
    @ColumnInfo(name = "servings")
    @Expose
    private Integer servings;


    @SerializedName("image")
    @ColumnInfo(name = "image")
    @Expose
    private String image;
    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    }
            ;

    protected Recipe(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.ingredients, (com.rangerthings.bakingapp.Model.Ingredient.class.getClassLoader()));
        in.readList(this.steps, (com.rangerthings.bakingapp.Model.Step.class.getClassLoader()));
        this.servings = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Recipe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}