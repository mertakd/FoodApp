package com.sisifos.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisifos.foodapp.databinding.RecipesRowLayoutBinding
import com.sisifos.foodapp.models.FoodRecipe
import com.sisifos.foodapp.models.Result
import com.sisifos.foodapp.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()
    //API'mizden her yeni veri aldığımızda(bu sınıfta setData veriyi alıp recipes değişkenine atıyoruz.), bu değişkeni bu yeni verilerle dolduracağız.

    class MyViewHolder(private val binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings() //data değiştiğinde layout içindeki veriyi günceller.
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
            //companion object içinde tanımlanan işlevler, RecipesAdapter sınıfının diğer öğelerine erişebilir. Bu, kodun daha düzenli ve okunabilir olmasını sağlar.
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil =
            RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)

        //notifydatasetchange tüm listeyi birdden güncelliyor ama diffutil sadece güncellenmesi gereken veriyi günceller.
        //recipe eski liste, newData yeni veri oluyor.
        //API'mizden ne zaman yeni bir veri getirsek, bu setData fonksiyonunu recipes fragment dan çağıracağız.
    }
}


/*bind vs setData
* bind() yöntemi, her bir liste öğesinin verilerini bağlar ve güncel tutar. setData() yöntemi ise tüm veri setini günceller ve yalnızca değişen öğeleri günceller. bind() yöntemi, RecyclerView.ViewHolder'ın içinde tanımlanırken, setData() yöntemi RecyclerView.Adapter'ın bir parçasıdır ve RecyclerView.Adapter sınıfının kendisi tarafından kullanılır.
* bind(result: Result) yöntemi, her bir liste öğesinin görünümünü (row item layout) belirli bir veri öğesiyle bağlar. Bu yöntem, RecyclerView.ViewHolder sınıfının içinde tanımlanır. ViewHolder sınıfı, RecyclerView tarafından kullanılan ve liste öğelerinin görünümlerini içeren bir bileşendir. bind() yöntemi, RecyclerView.ViewHolder'ın örneklendiği ve RecyclerView tarafından görüntülenen her bir liste öğesi için çağrılır.
* bind() yöntemi içinde binding.result = result ifadesi kullanılarak, row item layout içindeki veri değişkenlerine (result değişkeni burada) veri öğelerinin atanmasını sağlar. Örneğin, result.title row item layout içindeki bir TextView'e atandığında, o liste öğesindeki başlık metni güncellenir. Bu yöntem, her bir liste öğesinin verilerini bağlar ve güncel tutar.
* Öte yandan, setData(newData: FoodRecipe) yöntemi, RecyclerView'daki tüm verileri güncellemek için kullanılır. Bu yöntem, RecyclerView.Adapter sınıfının bir parçasıdır. Yeni bir veri seti (newData) parametre olarak alınır ve eski veri seti ile karşılaştırılır. DiffUtil sınıfı, eski ve yeni veri setleri arasındaki farkları tespit etmek için kullanılır. Bu farklılıklar, hangi öğelerin eklendiği, çıkarıldığı veya değiştirildiği gibi bilgileri içerir.
* DiffUtil sınıfının calculateDiff() yöntemi, eski ve yeni veri setleri arasındaki farkları hesaplar ve bir DiffUtil.DiffResult nesnesi döndürür. Bu nesne, farklılıkları içeren bir veri yapısıdır. Ardından dispatchUpdatesTo(this) yöntemi, bu farklılıkları RecyclerView.Adapter'a bildirir ve yalnızca değişen öğeleri günceller. Bu, gereksiz veri değişikliklerini önler ve performansı artırır.*/


/*
*bind vs setData2
*MyViewHolder, onCreateViewHolder, onBindViewHolder ve getItemCount gibi fonksiyonlar, RecyclerView.Adapter sınıfının işlevselliğini sağlamak için row layout ile ilgilenir.
*setData fonksiyonu, RecyclerView.Adapter sınıfına yeni verileri sağlar ve verilerin güncellenmesini yönetir. Bu fonksiyon, API'den yeni bir veri kümesi alındığında çağrılır ve recipes değişkenini günceller. Daha sonra, RecipesDiffUtil ve DiffUtil.calculateDiff() kullanılarak eski ve yeni veriler arasındaki farklar hesaplanır ve dispatchUpdatesTo(this) ile sadece güncellenmesi gereken verilerin RecyclerView'a bildirilmesi sağlanır. Bu sayede, tüm liste yerine sadece değişikliklerin güncellenmesi sağlanır.
*Yani, setData fonksiyonu verilerin güncellenmesi ve RecyclerView'ın yeniden düzenlenmesiyle ilgilenirken, diğer fonksiyonlar row layout ile ilgili işlemleri gerçekleştirir.  */


/*VERİ AKIŞI
*RecipesFragment oluşturulur ve onCreateView yöntemi çağrılır.
*RecipesFragment içindeki RecyclerView'a bir adaptör atanır ve adaptör oluşturulur.
*RecipesAdapter'ın setData yöntemi, API'den alınan yeni veri setini alır ve adaptörün iç veri listesini günceller.
*setData yöntemi içinde, farklılık hesaplaması için RecipesDiffUtil kullanılır ve sonuçlar adaptöre uygulanır.
*Adaptör, güncellenmiş verileri içeren onBindViewHolder yöntemini çağırarak her bir öğenin görünümünü günceller.
*XML layout dosyası, veri bağlama (data binding) kullanılarak görünümlere dinamik olarak verileri yansıtır.
*Güncellenmiş veriler, RecyclerView'da görüntülenir ve kullanıcıya gösterilir.
*
*
*setData() fonksiyonu API'den aldığı verileri newData parametresi olarak alır ve bu verileri recipes adlı bir listede saklar. recipes listesi, Result tipindeki verileri tutar.
*İlk olarak, RecipesDiffUtil sınıfı aracılığıyla mevcut recipes listesi ve yeni veri seti (newData.results) arasındaki farklılıklar hesaplanır. Bu farklılıklar, güncellemelerin optimize edilmesini sağlayan DiffUtil sınıfı tarafından işlenir
* Daha sonra, recipes listesi, newData.results ile güncellenir ve diffUtilResult.dispatchUpdatesTo(this) kullanılarak güncellemeler RecyclerView adaptörüne iletir.
* Bu sayede, setData() fonksiyonu, API'den alınan yeni veri setini kullanarak recipes listesini günceller ve bu güncellemeleri RecyclerView'a ileterek verilerin görüntülenmesini sağlar.
* OnBindViewHolder da da recipes listesi zaten xml ile bind edildiği için, xml içinden resul nesnelerine ulaşılır xml içinde bind edilir.onbinview holder her bir item ı teker teker yazdırır. */