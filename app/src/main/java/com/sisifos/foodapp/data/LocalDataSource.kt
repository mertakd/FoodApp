package com.sisifos.foodapp.data

import com.sisifos.foodapp.data.database.RecipesDao
import com.sisifos.foodapp.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }



    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }




}