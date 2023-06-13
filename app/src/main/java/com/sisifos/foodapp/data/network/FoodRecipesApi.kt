package com.sisifos.foodapp.data.network

import com.sisifos.foodapp.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>





}



/*FoodRecipesApi  INTERFACE BİR ŞABLONDUR
*FoodRecipesApi arabirimindeki getRecipes fonksiyonu, API isteğini tanımlar ve gerçek bir API çağrısı yapmaz. Bu fonksiyon, Retrofit kütüphanesi tarafından kullanılmak üzere API isteği için bir şablondur. FoodRecipesApi arayüzü, API isteğinin yapısı, parametreleri ve dönüş değeriyle ilgili bilgileri sağlar.
*Öte yandan, RemoteDataSource sınıfı, gerçek API isteklerini gerçekleştiren ve dönen verileri yöneten bir sınıftır. Bu sınıf, FoodRecipesApi arayüzünü kullanarak API isteklerini yapar ve istenen verileri elde eder.
*RemoteDataSource sınıfında yer alan getRecipes fonksiyonu, API isteğini gerçekleştirmek için FoodRecipesApi arayüzündeki getRecipes fonksiyonunu çağırır. İsteği yapmak için queries parametresini kullanır ve sonucunda Response<FoodRecipe> nesnesini döndürür.
*Bu iki ayrı sınıfın kullanılmasının nedeni, kodun parçalara ayrılması ve sorumlulukların ayrıştırılmasıdır. FoodRecipesApi sadece API isteği yapısını tanımlar ve Retrofit kütüphanesi tarafından kullanılır. RemoteDataSource ise API isteklerini gerçekleştirir ve dönen verileri yönetir. Bu sayede, kodun daha modüler, okunabilir ve bakımı kolay hale gelir.
*Kısacası, FoodRecipesApi sadece API isteklerinin tanımını sağlar, RemoteDataSource ise bu istekleri gerçekleştirir ve dönen verileri yönetir. İki ayrı sınıfın kullanılması, kodun yapısını ve sorumluluklarını düzenlemeye yardımcı olur.     */



/*
* Response sınıfı, Retrofit kütüphanesine ait bir sınıftır ve API yanıtlarını temsil eder. Bu sınıfın generic türü, yanıttaki verilerin doğru türde bir veri sınıfı ile eşleştiğini belirtir.

FoodRecipe ise, Response sınıfının parametre olarak aldığı generic türdür. Bu durumda, FoodRecipe veri sınıfı, Response nesnesi içindeki verileri temsil eder. Yani, eğer API isteği başarılı olduysa ve yanıtta FoodRecipe türünde veriler varsa, Response<FoodRecipe> nesnesi bu verilere erişmenizi sağlar.

Bu sayede, Response<FoodRecipe> türünü kullanarak API yanıtlarını alırken, Retrofit otomatik olarak gelen verileri FoodRecipe veri sınıfı ile eşleştirir. Böylece, istediğiniz verilere kolayca erişebilirsiniz.*/