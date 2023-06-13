package com.sisifos.foodapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.sisifos.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.sisifos.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.sisifos.foodapp.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.sisifos.foodapp.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.sisifos.foodapp.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.sisifos.foodapp.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.sisifos.foodapp.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.sisifos.foodapp.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)
//"foody_preferences" bu ad altında tüm key ler kaydedilecek.


@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    /*
    * Bu sınıf, kullanıcının seçtiği yemek ve diyet türü tercihlerini kalıcı olarak saklamak ve
    * bu tercihlere erişim sağlamak için Android Jetpack DataStore kütüphanesini kullanan bir veri deposu işlevselliği sağlar. */

    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)

        //oluşturulan anahtarlar - KEY
        //key ler type-value şeklinde oluşturulur. ör stringPreferencesKey string türünde, value değeri mealType(PREFERENCES_MEAL_TYPE)
        //Her bir çipin adını ve id sini kaydediyoruz. read yapabilmemiz için yani uygulayabilmemiz için id sini de kaydediyoruz
    }


    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }





    private val dataStore: DataStore<Preferences> = context.dataStore
    /*
    * private val dataStore: DataStore<Preferences> = context.dataStore satırı: Bu satır, DataStore nesnesini oluşturur ve
    * context.dataStore ifadesini kullanarak ilgili veri deposuna erişir.
    * context parametresi, DataStore nesnesinin hangi bağlamda kullanılacağını belirtir.*/





    //burada yukarıda ki key leri kaydediyoruz.
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId

            //data store, prefences değeri ile, yukarıda ki keyleri kaydeder.
            //key name:PreferenceKeys.selectedMealType,   value: = mealType
            //keyleri parametre olarak alıp kaydedecek.
        }
    }
    /*
    * suspend fun saveMealAndDietType fonksiyonu: Bu fonksiyon, kullanıcının seçtiği yemek ve diyet türü değerlerini alır ve bu değerleri veri deposuna kaydeder. Bu işlem, dataStore.edit işlevini kullanarak yapılır.
      mealType ve dietType parametreleri kullanıcının seçtiği yemek ve diyet türlerini temsil ederken, mealTypeId ve dietTypeId parametreleri bu tercihlerin kimliklerini temsil eder.*/

    /*
    *Bu saveMealAndDietType işlevi, kullanıcının seçtiği yemek ve diyet özelliklerini (mealType, mealTypeId, dietType, dietTypeId) alır ve bu değerleri veri deposunda (dataStore) saklar.
    *Bu işlevi kullanarak, kullanıcının seçtiği yemek ve diyet özelliklerini kaydedebilirsiniz. Örneğin, kullanıcının bir yemek türü seçtiğinde ve bu seçim üzerine bu işlevi çağırdığınızda, seçilen yemek türünün değeri (mealType) ve kimliği (mealTypeId) dataStore üzerinde ilgili anahtarlarla birlikte saklanır.
    * Bu sayede, kullanıcının seçtiği özellikler uygulama başlatıldığında veya başka bir zamanda ihtiyaç duyulduğunda geri alınabilir ve kullanılabilir. Bu veriler, uygulamanın durumuyla ilgili olarak kalıcı bir şekilde saklanır ve kullanıcının tercihlerini hatırlamak için kullanılabilir. */







    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences()) //Eğer yakalanan istisna bir IOException ise, emptyPreferences() işlevi çağrılarak boş bir tercihler nesnesi oluşturulur ve bu nesne akışa yayınlanır. Bu yaklaşım, hata durumunda akışın kesilmesini önlemek için kullanılır. Hata durumunda, istemciye boş bir tercihler nesnesi sağlanır ve uygulamanın düzgün bir şekilde çalışmaya devam etmesi sağlanır.
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE //herhangi bir chip seçilmediyse default olarak atanacak değer.
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    /*
    * map işlemi, dataStore.data akışındaki her bir öğeyi dönüştürerek yeni bir akış oluşturuyor. İşlem, dataStore.data akışındaki Preferences öğelerini MealAndDietType nesnelerine dönüştürüyor.
    * map işlemi, her bir Preferences öğesi üzerinde çalışır ve bu öğeleri selectedMealType, selectedMealTypeId, selectedDietType ve selectedDietTypeId özelliklerine sahip bir MealAndDietType nesnesine dönüştürür. Bu dönüşüm işlemi, preferences nesnesi içindeki ilgili anahtarların değerlerini alır ve yeni MealAndDietType nesnesini oluşturur.
    * Örneğin, selectedMealType değeri, preferences nesnesinin PreferenceKeys.selectedMealType anahtarına karşılık gelen değeridir. Eğer PreferenceKeys.selectedMealType anahtarına karşılık gelen değer null ise, DEFAULT_MEAL_TYPE değeri kullanılır. Bu şekilde, her bir öğe dönüştürülerek yeni bir MealAndDietType nesnesi oluşturulur.
    *Sonuç olarak, map işlemi dataStore.data akışını dönüştürerek Flow<MealAndDietType> tipinde bir akış elde eder. Bu akış, MealAndDietType nesnelerini temsil eder ve daha sonra bu verileri kullanarak ilgili işlemleri gerçekleştirebilirsiniz. */


    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }

}


data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)

/*data class MealAndDietType
*MealAndDietType sınıfı bir veri taşıyıcısı olarak kullanılır.
*DataStoreRepository sınıfı, bu veri yapısını kullanarak kullanıcının tercihlerini saklar ve okur. Bu sınıfın kullanılması, verilerin daha düzenli ve yapılandırılmış bir şekilde taşınmasını sağlar. Aynı zamanda, veri sınıfının içerisinde yer alan özellikler üzerinden tercihlerle ilgili işlemler yapılabilir ve bu özelliklere kolayca erişim sağlanabilir.  */










/* DataStoreReposiry Görevi
*bu sınıfta seçilen çiplerle istek atılmaz, bu DataStoreRepository sınıfı sadece kullanıcının seçtiği yemek ve diyet türü tercihlerini kalıcı olarak saklamak ve bu tercihlere erişim sağlamak için tasarlanmıştır. Bu sınıf, tercihlerin saklanmasını ve okunmasını sağlayan bir veri deposu işlevselliği sağlar. Ancak, sınıf içinde seçilen chip tercihlerine istek atılmaz.
*DataStoreRepository sınıfının amacı, tercihlerin kalıcı olarak saklanması ve erişimi sağlanmasıdır. Bunun yanı sıra, bu tercihlerin uygulamanın diğer bölümlerinde kullanılabilmesini sağlar. Örneğin, kullanıcı yemek veya diyet tercihlerini seçtikten sonra bu tercihler DataStore'da saklanır ve diğer bölümlerde bu tercihlere erişilebilir. Ancak, bu sınıf, tercihlerin sunucuya veya başka bir yerde istek atılmasını sağlamaz. Bu sınıf, yalnızca tercihlerin yerel olarak saklanması ve erişilmesi için kullanılır.
*
* Bu DataStoreRepository sınıfı, kullanıcının seçtiği yemek ve diyet türü tercihlerini kalıcı olarak saklamak ve bu tercihlere erişim sağlamak için Android Jetpack DataStore kütüphanesini kullanan bir veri deposu işlevselliği sağlar.
* Genel olarak, bu sınıf şu işlevleri yerine getirir:
* Kullanıcının seçtiği yemek ve diyet türü tercihlerini kalıcı olarak saklamak için DataStore kullanır.
* Kullanıcının çevrimdışı modda olup olmadığını saklamak için DataStore kullanır.
* Kullanıcının seçtiği yemek ve diyet türü tercihlerini okumak için Flow akışını kullanır. Bu sayede, tercihlerdeki değişiklikleri dinleyebilir ve güncel değerlere erişebilirsiniz.
* Kullanıcının çevrimdışı modda olup olmadığını okumak için Flow akışını kullanır.
* Kullanıcının seçtiği yemek ve diyet türü tercihlerini kaydetmek için saveMealAndDietType işlevini sağlar.
*Kullanıcının çevrimdışı mod durumunu kaydetmek için saveBackOnline işlevini sağlar. */