package com.sisifos.foodapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {

        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}

//veriyi her zaman değil sadece bir kez gözlemler live data bu kod bloğu ile.

//observeOnce: bu yapıyı kullanmadığımız zaman yani sadece observe yi kullandığımız zaman, uygulamayı silip yeniden yüklediğimiz de: hem requestapidata hem de readdatabase çağrılıyor. Ama observeOnce kullanıldığında  uygulama silinip yüklendiğinde sadece reqestApiData çağrılıyor.