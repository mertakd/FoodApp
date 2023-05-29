package com.sisifos.foodapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sisifos.foodapp.models.FoodRecipe
import com.sisifos.foodapp.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false) //apiden gelen tüm veriler değişir. room veritabanı her okunduğunda en yeni veriler gelir
    var id: Int = 0

    //tek bir row u temsil ediyor RecipesEntity. Bu RecipesEntity, FoodRecipe tarafından doldurulacak.
}