package com.sisifos.foodapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sisifos.foodapp.models.FoodRecipe

class RecipesTypeConverter {

    var gson = Gson()

    //nesneyi stringe çeviriyor
    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String{
        return gson.toJson(foodRecipe)
    }



    //stringi nesneye çeviriyor.
    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe{
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }
}