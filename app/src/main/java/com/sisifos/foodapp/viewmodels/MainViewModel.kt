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
     * ROOM
     */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    /**
     * RETROFIT
     */
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

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isEmpty() -> {
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
}

/*
* @HiltViewModel
*@HiltViewModel bir Hilt anotasyonudur ve ViewModel sınıflarının Hilt tarafından yönetilmesini sağlar.
*Bu anotasyonu bir ViewModel sınıfına eklediğinizde, Hilt bu sınıfın örneğini oluşturur ve ViewModel'in bağımlılıklarını enjekte etmesini sağlar. Bu sayede, ViewModel'in bağımlılıklarını elle yönetmek veya örneğini oluşturmak için ekstra kod yazmanıza gerek kalmaz.
*@HiltViewModel ayrıca, ViewModel'in yaşam döngüsünü ve etkileşimlerini doğru bir şekilde yönetmek için Hilt tarafından oluşturulan HiltViewModelFactory ile birlikte çalışır. Bu fabrika sınıfı, ViewModel'in oluşturulması ve bağımlılıklarının enjekte edilmesi için gereken işlemleri gerçekleştirir.
*Bu örnekte @HiltViewModel anotasyonu, MainViewModel sınıfının Hilt tarafından yönetilen bir ViewModel olduğunu belirtir. Hilt, bu sınıfın örneğini oluştururken Repository bağımlılığını enjekte eder ve Application nesnesini sağlar. Böylece, MainViewModel içinde Repository ve Application nesnelerine erişebilirsiniz.    */