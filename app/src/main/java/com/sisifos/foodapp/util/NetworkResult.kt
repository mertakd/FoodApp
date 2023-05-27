package com.sisifos.foodapp.util

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T): NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null): NetworkResult<T>(data, message)
    class Loading<T>: NetworkResult<T>()

}


//bir genel ağ isteği sonucunu temsil etmek için kullanılır sealed class

/**
 * NetworkResult<T>: Genel sealed class'ın kendisi olup, data ve message adında iki özelliği içerir. data, ağ isteği sonucunda dönen veriyi temsil ederken, message ise istekle ilgili bir hata veya durum mesajını temsil eder. Her iki özellik de varsayılan olarak null değerine sahiptir.

Success<T>: NetworkResult<T>'ın alt sınıfıdır ve başarılı bir ağ isteği sonucunu temsil eder. İstenen veri, data özelliği aracılığıyla sağlanır.

Error<T>: NetworkResult<T>'ın alt sınıfıdır ve bir hata durumunu temsil eder. İstenen veri, data özelliği aracılığıyla sağlanabilir ve hata mesajı, message özelliği aracılığıyla temsil edilir.

Loading<T>: NetworkResult<T>'ın alt sınıfıdır ve ağ isteğinin yükleniyor olduğunu temsil eder. Bu durumda, data ve message özellikleri kullanılmaz.

Bu yapı, ağ isteklerinin sonuçlarını belirtmek ve bu sonuçları işlemek için kullanılabilir. Örneğin, bir API çağrısının sonucunu NetworkResult türünde döndürerek, başarılı bir isteğin verisini alabilir veya hatalı bir durumda hata mesajını kontrol edebilirsiniz. Ayrıca, yükleme durumunu belirlemek için de kullanılabilir.*/