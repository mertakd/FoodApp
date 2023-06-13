package com.sisifos.foodapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sisifos.foodapp.models.FoodRecipe
import com.sisifos.foodapp.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {

    @PrimaryKey(autoGenerate = false) //apiden gelen tüm veriler değişir. room veritabanı her okunduğunda en yeni veriler gelir
    var id: Int = 0

    //tek bir row u temsil ediyor RecipesEntity. Bu RecipesEntity, FoodRecipe tarafından doldurulacak.
    //veritabanımızın tek bir row u var : FoodRecipe
}


/*
* id adlı bir alan, birincil anahtar olarak işaretlenir. autoGenerate parametresi false olarak ayarlanır, bu da birincil anahtarın otomatik olarak oluşturulmayacağını ve manuel olarak atanacağını gösterir. Sıfır (0) değeri, bu id alanının varsayılan değerini temsil eder.
* RecipesEntity sınıfını Room veritabanı tablosu olarak kullanmak için gerekli işaretlemeleri içerir. Bu şekilde, API'den alınan FoodRecipe verilerini RecipesEntity nesneleri olarak Room veritabanına kaydedebilirsiniz. Böylece verileri önbelleğe alabilir, daha sonra gerektiğinde kullanabilir ve gerektiğinde güncelleyebilirsiniz.*/