package com.sisifos.foodapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //apiden yeni veri çektiğimizde eski veriyle değiştirir replace.Bu nedenle, veritabanı tablomuz temelde her yeni tarif istediğimizde bir veritabanında en yeni tariflerimizi içerecektir
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

}