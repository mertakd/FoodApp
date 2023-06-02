package com.sisifos.foodapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sisifos.foodapp.data.DataStoreRepository
import com.sisifos.foodapp.data.MealAndDietType
import com.sisifos.foodapp.util.Constants
import com.sisifos.foodapp.util.Constants.Companion.API_KEY
import com.sisifos.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.sisifos.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.sisifos.foodapp.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.sisifos.foodapp.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.sisifos.foodapp.util.Constants.Companion.QUERY_API_KEY
import com.sisifos.foodapp.util.Constants.Companion.QUERY_DIET
import com.sisifos.foodapp.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.sisifos.foodapp.util.Constants.Companion.QUERY_NUMBER
import com.sisifos.foodapp.util.Constants.Companion.QUERY_SEARCH
import com.sisifos.foodapp.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor (
    application: Application,
    private val dataStoreRepository: DataStoreRepository
    ): AndroidViewModel(application) {

    /*Bu RecipesViewModel sınıfı, yemek tarifi verilerini yönetmek, kullanıcının seçtiği yemek ve diyet özelliklerini kaydetmek ve sorguları oluşturmak gibi işlemleri gerçekleştirir.
      Bu işlevler, kullanıcı arayüzünden gelen isteklere yanıt vermek ve ilgili verileri sağlamak için kullanılabilir.*/

    var networkStatus = false
    var backOnline = false

    private lateinit var mealAndDiet: MealAndDietType
    /*
    * private lateinit var mealAndDiet: MealAndDietType: Bu değişken, kullanıcının seçtiği yemek ve diyet özelliklerini (MealAndDietType) tutar.
    * İlk olarak başlatılmaz ve saveMealAndDietTypeTemp işlevi tarafından ayarlanır.*/





    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    /*
    *  Bu özellik, dataStoreRepository üzerinden alınan yemek ve diyet özelliklerinin Flow nesnesini temsil eder.
    *  Bu Flow, yemek ve diyet özelliklerinin güncel değerlerini yayınlar.*/
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()


    fun saveMealAndDietTypeTemp(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        mealAndDiet = MealAndDietType(
            mealType,
            mealTypeId,
            dietType,
            dietTypeId
        )
    }
    /*
    * fun saveMealAndDietTypeTemp(): Bu işlev, geçici olarak yemek ve diyet özelliklerini mealAndDiet değişkenine kaydeder.
    * Bu geçici kayıt, daha sonra saveMealAndDietType işlevinde kalıcı olarak kaydedilmek üzere kullanılır.*/








    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@RecipesViewModel::mealAndDiet.isInitialized) {
                dataStoreRepository.saveMealAndDietType(
                    mealAndDiet.selectedMealType,
                    mealAndDiet.selectedMealTypeId,
                    mealAndDiet.selectedDietType,
                    mealAndDiet.selectedDietTypeId
                )
            }
        }
    /*
    *  Bu işlev, mealAndDiet değişkeninin başlatıldığı durumda, yemek ve diyet özelliklerini dataStoreRepository üzerinde kaydeder.
    *  Bu işlev, viewModelScope içinde IO dağıtıcısında çalışır.*/


    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }



    fun applyQueries(): HashMap<String, String> {

        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        if (this@RecipesViewModel::mealAndDiet.isInitialized) {
            queries[QUERY_TYPE] = mealAndDiet.selectedMealType
            queries[QUERY_DIET] = mealAndDiet.selectedDietType
        } else {
            queries[QUERY_TYPE] = DEFAULT_MEAL_TYPE
            queries[QUERY_DIET] = DEFAULT_DIET_TYPE
        }

        return queries
    }
    /*
    *Bu işlev, yemek tarifi API'sine gönderilecek istekleri yapılandırmak için kullanılır.
     Sorgu parametreleri, yemek türü ve diyet türü gibi kullanıcı tarafından seçilen özelliklere göre belirlenir.
     Eğer mealAndDiet değişkeni başlatılmışsa, kullanıcının seçtiği yemek ve diyet özellikleri, aksi takdirde varsayılan değerler kullanılır.
     *
    * Bu applyQueries() işlevi, yemek tarifi sorgularını içeren bir HashMap<String, String> nesnesi oluşturur ve döndürür. Bu sorgular, API'den yemek tarifleri almak için kullanılacak parametreleri belirtir.
    * İşlevin adıyla uyumlu olarak, applyQueries() işlevi, ilgili sorgu parametrelerini ayarlamak için kullanılır. İşlevin içeriği aşağıdaki adımları takip eder:
    * Boş bir HashMap olan queries oluşturulur: HashMap<String, String>()
    *Belirli sorgu parametreleri queries nesnesine eklenir. Örnek olarak:
    QUERY_NUMBER anahtarı, DEFAULT_RECIPES_NUMBER değeriyle ilişkilendirilir.
    QUERY_API_KEY anahtarı, API_KEY değeriyle ilişkilendirilir.
    QUERY_ADD_RECIPE_INFORMATION anahtarı, "true" değeriyle ilişkilendirilir.
    QUERY_FILL_INGREDIENTS anahtarı, "true" değeriyle ilişkilendirilir.
    *mealAndDiet değişkeni başlatılmışsa, yemek ve diyet özelliklerine ait sorgu parametreleri queries nesnesine eklenir. Bunun için aşağıdaki adımlar izlenir:
    QUERY_TYPE anahtarı, mealAndDiet değişkeninin selectedMealType özelliğiyle ilişkilendirilir.
    QUERY_DIET anahtarı, mealAndDiet değişkeninin selectedDietType özelliğiyle ilişkilendirilir.
    *mealAndDiet değişkeni başlatılmamışsa, varsayılan yemek ve diyet özellikleri queries nesnesine eklenir. Bunun için aşağıdaki adımlar izlenir:
    QUERY_TYPE anahtarı, DEFAULT_MEAL_TYPE değeriyle ilişkilendirilir.
    QUERY_DIET anahtarı, DEFAULT_DIET_TYPE değeriyle ilişkilendirilir.
    *Son olarak, queries nesnesi döndürülür, böylece API isteği için kullanılabilecek tüm sorgu parametreleri içerilmiş olur.*/



    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }





}