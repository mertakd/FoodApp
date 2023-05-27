package com.sisifos.foodapp.data

import com.sisifos.foodapp.data.network.FoodRecipesApi
import com.sisifos.foodapp.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    //buraya FoodRecipesApi ı inject ediyoruz. provideApiService fonksiyonu tek bir örneğini oluşturuyor.


    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

}