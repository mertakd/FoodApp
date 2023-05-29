package com.sisifos.foodapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.sisifos.foodapp.R

class RecipesRowBinding {

    companion object{

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600) //solma efekti veriyor, 600 milisaniyede.
                error(R.drawable.ic_error_placeholder)
            }
        }




        //burdda yazdığımız fonksiyonlara her yerden erişmek için companion object

        @BindingAdapter("setNumberOfLikes") //xml içinden buraya yazdığımız isim ile ulaşıyoruz.
        @JvmStatic //satatik yapıyor ki her yerden erişelim.
        fun setNumberOfLikes(textView: TextView, likes: Int){
            textView.text = likes.toString()
            //api den gelen text verisi integer olarak geliyor, bu yüzden integer ı string e çevirmeliyiz.
        }


        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView, minutes: Int){
            textView.text = minutes.toString()
        }



        //leaf image için: eğer gelen veri vegan ise true döndürür ve hem text hem de image yeşil olur. false ise resim ve text gri olur.
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }






    }



}