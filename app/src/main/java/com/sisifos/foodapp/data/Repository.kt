package com.sisifos.foodapp.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {

    val remote = remoteDataSource
    val local = localDataSource
}

/*
* @ViewModelScoped
*@ViewModelScoped bir Hilt anotasyonudur ve bir sınıfın ViewModel tarafından kullanılması durumunda yaşam döngüsünün yönetilmesini sağlar.
*Bu anotasyon, bir sınıfın ViewModel içinde kullanılması durumunda, sınıfın örneğinin ViewModel ömrüyle aynı olmasını sağlar. Yani, ViewModel'ın yaşam döngüsü boyunca aynı örneğin kullanılmasını garanti eder. Örneğin, bir ViewModel oluşturulduğunda Repository sınıfının da aynı örneğinin kullanılmasını sağlar.
*@ViewModelScoped kullanmak, ViewModel içindeki bağımlılıkları yönetmek ve ViewModel'ın her bir istemci tarafından ayrı bir örneği olmasını engellemek için faydalıdır. Bu, aynı ViewModel örneği üzerinde çalışan farklı bileşenlerin (örneğin, aktiviteler veya fragmentlar) tutarlı bir şekilde aynı Repository örneğine erişmelerini sağlar.
*Bu örnekte @ViewModelScoped anotasyonu, Repository sınıfının ViewModel ömrüyle aynı ömrü paylaşmasını sağlar. Bu, ViewModel içindeki diğer sınıfların (remoteDataSource ve localDataSource) Repository sınıfının aynı örneğini kullanmalarını sağlar. Bu sayede, ViewModel'ın yaşam döngüsü boyunca aynı veri kaynaklarının kullanılması sağlanır.   */