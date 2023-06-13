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


/*FoodRecipesApi  INTERFACE, BİR ŞABLONDUR.
*FoodRecipesApi arabirimindeki getRecipes fonksiyonu, API isteğini tanımlar ve gerçek bir API çağrısı yapmaz. Bu fonksiyon, Retrofit kütüphanesi tarafından kullanılmak üzere API isteği için bir şablondur. FoodRecipesApi arayüzü, API isteğinin yapısı, parametreleri ve dönüş değeriyle ilgili bilgileri sağlar.
*Öte yandan, RemoteDataSource sınıfı, gerçek API isteklerini gerçekleştiren ve dönen verileri yöneten bir sınıftır. Bu sınıf, FoodRecipesApi arayüzünü kullanarak API isteklerini yapar ve istenen verileri elde eder.
*RemoteDataSource sınıfında yer alan getRecipes fonksiyonu, API isteğini gerçekleştirmek için FoodRecipesApi arayüzündeki getRecipes fonksiyonunu çağırır. İsteği yapmak için queries parametresini kullanır ve sonucunda Response<FoodRecipe> nesnesini döndürür.
*Bu iki ayrı sınıfın kullanılmasının nedeni, kodun parçalara ayrılması ve sorumlulukların ayrıştırılmasıdır. FoodRecipesApi sadece API isteği yapısını tanımlar ve Retrofit kütüphanesi tarafından kullanılır. RemoteDataSource ise API isteklerini gerçekleştirir ve dönen verileri yönetir. Bu sayede, kodun daha modüler, okunabilir ve bakımı kolay hale gelir.
*Kısacası, FoodRecipesApi sadece API isteklerinin tanımını sağlar, RemoteDataSource ise bu istekleri gerçekleştirir ve dönen verileri yönetir. İki ayrı sınıfın kullanılması, kodun yapısını ve sorumluluklarını düzenlemeye yardımcı olur.     */


/*
*SÜRECİN İŞLEME ŞEKLİ
*FoodRecipesApi arabirimindeki getRecipes fonksiyonu, API isteğini tanımlar ve gerçek bir API çağrısı yapmaz. Bu fonksiyon, Retrofit kütüphanesi tarafından kullanılmak üzere API isteği için bir şablondur. FoodRecipesApi arayüzü, API isteğinin yapısı, parametreleri ve dönüş değeriyle ilgili bilgileri sağlar.
*Öte yandan, RemoteDataSource sınıfı, gerçek API isteklerini gerçekleştiren ve dönen verileri yöneten bir sınıftır. Bu sınıf, FoodRecipesApi arayüzünü kullanarak API isteklerini yapar ve istenen verileri elde eder.
*RemoteDataSource sınıfında yer alan getRecipes fonksiyonu, API isteğini gerçekleştirmek için FoodRecipesApi arayüzündeki getRecipes fonksiyonunu çağırır. İsteği yapmak için queries parametresini kullanır ve sonucunda Response<FoodRecipe> nesnesini döndürür.
*Bu iki ayrı sınıfın kullanılmasının nedeni, kodun parçalara ayrılması ve sorumlulukların ayrıştırılmasıdır. FoodRecipesApi sadece API isteği yapısını tanımlar ve Retrofit kütüphanesi tarafından kullanılır. RemoteDataSource ise API isteklerini gerçekleştirir ve dönen verileri yönetir. Bu sayede, kodun daha modüler, okunabilir ve bakımı kolay hale gelir.
*Kısacası, FoodRecipesApi sadece API isteklerinin tanımını sağlar, RemoteDataSource ise bu istekleri gerçekleştirir ve dönen verileri yönetir. İki ayrı sınıfın kullanılması, kodun yapısını ve sorumluluklarını düzenlemeye yardımcı olur.    */