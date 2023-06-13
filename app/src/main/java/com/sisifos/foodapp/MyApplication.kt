package com.sisifos.foodapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
}

//dagger bileşenlerinin otomatik olarak oluşturulduğu yer.

/*
*Hilt, bağımlılıkların yaşam döngüsünü doğru şekilde yönetmek için Android uygulamasının yaşam döngüsüyle bütünleşir. Bu nedenle, Hilt'in doğru çalışması için bir Application sınıfının oluşturulması gerekmektedir.
*Application sınıfı, Android uygulamasının temel sınıfıdır ve uygulama başlatıldığında ilk olarak yürütülen sınıftır. Hilt, bağımlılıkları uygulama düzeyinde yönetmek için Application sınıfını kullanır. Hilt'in işlevselliğini sağlamak ve bağımlılıkları enjekte etmek için Hilt modülünün @HiltAndroidApp anotasyonu ile işaretlenmiş bir Application sınıfına ihtiyaç duyar.
*Bu Application sınıfı, Hilt tarafından otomatik olarak oluşturulacak ve Hilt bileşenlerinin ve modüllerinin başlatılmasını sağlayacaktır. Ayrıca, Hilt tarafından oluşturulan bileşenlerin doğru bir şekilde uygulama yaşam döngüsüyle yönetilmesini sağlayacak olan Hilt bileşenini de içerecektir.
* Bu nedenle, Hilt kullanırken bir Application sınıfı oluşturmak ve bu sınıfı @HiltAndroidApp anotasyonuyla işaretlemek önemlidir. Bu, Hilt'in uygulama düzeyinde bağımlılıkları yönetmesini ve bağımlılıkları enjekte etmesini sağlayacaktır.  */