package com.sisifos.foodapp.di

import com.sisifos.foodapp.util.Constants.Companion.BASE_URL
import com.sisifos.foodapp.data.network.FoodRecipesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    //return tipleri hilt kütüphanesine haber veriyor
    //singleton: bu bağımlılıkların her birinin yalnızca bir örneği olacak.
    //artık api yi remote data source a inject edebiliriz.


    @Singleton
    @Provides
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    //burada API hizmetlerimizi yerine getirmek için retrofit örneğini nasıl sağlayacağımızı belirledik.
    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FoodRecipesApi {
        return retrofit.create(FoodRecipesApi::class.java)
    }


}

//API yi remote data source a inject edebileceğiz.
//food recipes api nin ihtiyaç duyduğu tüm bağımlılıkları burada karşılıyoruz kısaca.


/*
*MODULE SINIFI NEDEN KULLANILIR?
*Hilt modülü oluşturarak bağımlılıkları tanımlayabilir, yapılandırabilir ve yönetebiliriz. Modül sınıfı, Hilt tarafından otomatik olarak bağımlılıkları oluşturmak için kullanılır ve Hilt bileşenlerine bağlanır.
*@Module anotasyonu, bir sınıfın Hilt modülü olduğunu belirtir. Bu modül sınıfı, Hilt tarafından bağımlılıkların sağlanacağı ve yapılandırılacağı yerdir. Modül sınıfı, bağımlılıkların nasıl oluşturulacağını, yapılandırılacağını veya dışarıdan sağlanacağını belirleyen metotlar içerir.
*Modül sınıfı, bağımlılıkların tek bir yerde yönetilmesini sağlar ve bu şekilde bağımlılıkların tekrar kullanılabilirliği, değiştirilebilirliği ve test edilebilirliği artırır. Modül sınıfı, bağımlılıkların oluşturulması için gerekli olan diğer bağımlılıkları (örneğin, API hizmeti için bir istemci nesnesi) sağlayabilir ve yapılandırabilir.
*@InstallIn anotasyonu ise Hilt'in hangi bileşenin veya bileşenlerin bağlamında bu modülün kullanılacağını belirtir. Örneğin, SingletonComponent bir bileşene modülün bağlandığında, bu modülde tanımlanan bağımlılıklar tek bir örnekle sınırlı olacak ve uygulama yaşam döngüsü boyunca paylaşılacak.
*@InstallIn anotasyonu ise Hilt'in hangi bileşenin veya bileşenlerin bağlamında bu modülün kullanılacağını belirtir. Örneğin, SingletonComponent bir bileşene modülün bağlandığında, bu modülde tanımlanan bağımlılıklar tek bir örnekle sınırlı olacak ve uygulama yaşam döngüsü boyunca paylaşılacak.
 */


/*
* BU SINIFTA Kİ FONKSİYONLARIN İŞLEVLERİ
* provideHttpClient metodu OkHttpClient türünde bir nesne oluşturur ve geri döndürür. Bu nesne, HTTP isteklerini göndermek ve almak için kullanılan bir kütüphanedir. Bu bağımlılık, diğer sınıfların OkHttpClient'i kullanması gerektiğinde enjekte edilir.
* Benzer şekilde, provideConverterFactory metodu GsonConverterFactory türünde bir nesne oluşturur ve geri döndürür. Bu nesne, JSON verilerini Java nesnelerine dönüştürmek için kullanılan bir dönüştürücüdür. Bu bağımlılık, diğer sınıfların JSON dönüşümlerini yaparken kullanması gerektiğinde enjekte edilir.
* provideRetrofitInstance metodu, OkHttpClient ve GsonConverterFactory bağımlılıklarını parametre olarak alarak Retrofit nesnesini oluşturur ve geri döndürür. Bu nesne, API hizmetlerine erişmek için kullanılan bir HTTP istemcisidir. Bu bağımlılık, API isteklerini gerçekleştirmek için diğer sınıflar tarafından kullanılmak üzere enjekte edilir.
* Son olarak, provideApiService metodu Retrofit bağımlılığını parametre olarak alarak FoodRecipesApi arayüzünün bir örneğini oluşturur ve geri döndürür. Bu arayüz, API isteklerini tanımlayan ve yapılandıran bir arayüzdür. Diğer sınıflar, API isteklerini yaparken bu arayüzü kullanarak enjekte edilen FoodRecipesApi örneğini kullanabilir.
* */