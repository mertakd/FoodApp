package com.sisifos.foodapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.sisifos.foodapp.data.Repository
import com.sisifos.foodapp.data.database.RecipesEntity
import com.sisifos.foodapp.models.FoodRecipe
import com.sisifos.foodapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject



@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /**
     *ROOM
     */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }



    /**
     *RETROFIT
     */

    //inject kullandığımız için viewmodel factory kullanmamıza gerek yok arka planda kendisi bazı kodları oluşturuyor.
    //kısaca yapılan işlem: istek atılır, bunların kontolü yapılır ve recipesResponse mutablelivedata sına kaydedilir.

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()



    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {

        recipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    /*
        Bu kod parçacığı, offlineCacheRecipes işlevini kullanarak FoodRecipe nesnesini RecipesEntity nesnesine dönüştürür ve
        ardından bu dönüştürülmüş veriyi insertRecipes işlevine geçerek veritabanına ekler.
        Bu, yemek tarifi verilerini çevrimdışı olarak önbelleğe almak için kullanılan bir adımdır.
     */

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout") //cevap gecikirse gelen hata timeout hatası olur
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isEmpty() -> { //cevap başarılı da olsa içi boş gelebilir api den.
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }


    //bu fonksiyon mevcut ağ bağlantısının internete erişimi olup olmadığını belirlemek için kullanılabilir. true değeri, internete erişim olduğunu gösterirken, false değeri internete erişimin olmadığını gösterir.
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


    /**
     * APPLICATION NE İŞE YARAR?
     * application parametresi, MainViewModel sınıfının bir bağımlılığını temsil eden Application sınıfının örneğidir. Bu parametre, ViewModel'in Android bileşenlerine (örneğin, aktivite veya uygulama) bağımlı olmasını ve bu bileşenlerle etkileşime girebilmesini sağlar.

    AndroidViewModel sınıfı, Android bileşenlerine özgü işlevselliği sağlamak üzere tasarlanmış bir ViewModel sınıfıdır. Bu sınıf, uygulama Context'ine erişmek için Application sınıfının bir örneğini gerektirir. ViewModel'in Application sınıfını almasının nedeni, Android bileşenleriyle etkileşim kurarken genellikle uygulama Context'ine ihtiyaç duyulmasıdır.

    Örneğin, ViewModel içinde uygulama ayarlarını almak, kaynaklara (dize, çizim, renk vb.) erişmek veya diğer Android bileşenleriyle (örneğin, başka bir aktivite başlatmak) etkileşime girmek gerekebilir. Bu nedenle, ViewModel'in Application örneğine ihtiyaç duyduğu ve application parametresi aracılığıyla bu bağımlılığı enjekte ettiği görülüyor.

    Bununla birlikte, application parametresinin kullanımı uygulama bağlamına özgüdür ve genel olarak ViewModel'in Android bileşenleriyle uyumlu bir şekilde çalışabilmesini sağlar.*/

    /**
     * Bu yapı, yemek tarifi verilerini almak ve bu verilere erişimi yönetmek için kullanılır.
     * recipesResponse değişkeni, API isteklerinin sonucunu saklar ve diğer bileşenlerin bu sonuçları gözlemlemesini sağlar. getRecipes fonksiyonu ise yemek tarifi verilerini almak için bir API isteği yapar ve sonucu recipesResponse üzerinden günceller.*/
}



/*
* @HiltViewModel
*@HiltViewModel bir Hilt anotasyonudur ve ViewModel sınıflarının Hilt tarafından yönetilmesini sağlar.
*Bu anotasyonu bir ViewModel sınıfına eklediğinizde, Hilt bu sınıfın örneğini oluşturur ve ViewModel'in bağımlılıklarını enjekte etmesini sağlar. Bu sayede, ViewModel'in bağımlılıklarını elle yönetmek veya örneğini oluşturmak için ekstra kod yazmanıza gerek kalmaz.
*@HiltViewModel ayrıca, ViewModel'in yaşam döngüsünü ve etkileşimlerini doğru bir şekilde yönetmek için Hilt tarafından oluşturulan HiltViewModelFactory ile birlikte çalışır. Bu fabrika sınıfı, ViewModel'in oluşturulması ve bağımlılıklarının enjekte edilmesi için gereken işlemleri gerçekleştirir.
*Bu örnekte @HiltViewModel anotasyonu, MainViewModel sınıfının Hilt tarafından yönetilen bir ViewModel olduğunu belirtir. Hilt, bu sınıfın örneğini oluştururken Repository bağımlılığını enjekte eder ve Application nesnesini sağlar. Böylece, MainViewModel içinde Repository ve Application nesnelerine erişebilirsiniz.    */