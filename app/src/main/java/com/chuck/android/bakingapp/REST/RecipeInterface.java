package com.chuck.android.bakingapp.REST;

import com.chuck.android.bakingapp.models.RecipeList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeInterface {
    //Get Recipes from Udacity
    @GET("baking.json")
    Call<List<RecipeList>> getCurrentRecipes();
}
