//package com.worldwidemobiledevelopment.recipes.adapters
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.SimpleAdapter
//import android.widget.TextView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.worldwidemobiledevelopment.recipes.R
//import com.worldwidemobiledevelopment.recipes.data.Meal
//import com.worldwidemobiledevelopment.recipes.data.PopularMeal
//import com.worldwidemobiledevelopment.recipes.data.RecommendItems
//import com.worldwidemobiledevelopment.recipes.data.TopMeal
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers.IO
//import kotlinx.coroutines.Dispatchers.Main
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//const val TOP_MEALS_SECTION = 1
//const val POPULAR_MEALS_SECTION = 2
//
//class MenuAdapter(private val recommendedItems: List<RecommendItems>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
////    private var itemDelegate : ItemDelegate? = null
//
////    fun updateTopMeals(topMeals: List<TopMeal>){
////        this.topMeals = topMeals
////        topMealsAdapter.submitList(topMeals)
////    }
//
////    fun updatePopularMeals(popularMeals: List<PopularMeal>){
////        this.popularMeals = popularMeals
////        popularMealsAdapter.submitList(popularMeals)
////    }
//
//
//    override fun getItemViewType(position: Int): Int {
//        return when (recommendedItems[position]) {
//            is TopMeal -> { TOP_MEALS_SECTION }
//            is PopularMeal -> { POPULAR_MEALS_SECTION }
//
//        }
//    }
//
//    override fun getItemCount(): Int = recommendedItems.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            TOP_MEALS_SECTION -> {
//                TopMealsViewHolder(
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.section_top_meals, parent, false)
//                )
//            }
//            POPULAR_MEALS_SECTION -> {
//                PopularMealsViewHolder(
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.section_popular_meals, parent, false)
//                )
//            }
//            else -> {
//                PopularMealsViewHolder(
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.section_popular_meals, parent, false)
//                )
//                // todo error occurred
//            }
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (recommendedItems[position]) {
//            is TopMeal -> {
//                holder.itemView.findViewById<RecyclerView>(R.id.rvTopMeals).layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
//                val topMealsAdapter = SimpleAdapter((recommendedItems[position] as TopMeal).subItems)
//                holder.itemView.findViewById<RecyclerView>(R.id.rvTopMeals).apply {
//                    setHasFixedSize(true)
//                    adapter = topMealsAdapter
//                }
//            }
//            is PopularMeal -> {
//                holder.itemView.findViewById<RecyclerView>(R.id.rvPopularMeals).layoutManager = GridLayoutManager(holder.itemView.context, 2)
//                val popularMealAdapter = com.worldwidemobiledevelopment.recipes.adapters.SimpleAdapter((recommendedItems[position] as PopularMeal).subItems)
//                holder.itemView.findViewById<RecyclerView>(R.id.rvPopularMeals).run {
//                    setHasFixedSize(true)
//                    adapter = popularMealAdapter
//                }
//            }
//        }
//    }
//
//    inner class TopMealsViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
//    }
//
//    inner class PopularMealsViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
//    }
////
//////    fun attachDelegate(itemDelegate: ItemDelegate){
//////        this.itemDelegate = itemDelegate
//////    }
////
//////    fun submitList(items: List<RecommendItems>) {
//////        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(ItemsDiffCallback(this.items, items))
//////        this.items = items
//////        diffResult.dispatchUpdatesTo(this)
//////    }
//////
//////    class ItemsDiffCallback(var oldList: List<RecommendItems>, var newList: List<RecommendItems>) :
//////        DiffUtil.Callback() {
//////        override fun getOldListSize(): Int = oldList.size
//////
//////        override fun getNewListSize(): Int = newList.size
//////
//////        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//////            return when (oldList[oldItemPosition]) {
//////                is TopMeal -> {
//////                    (oldList[oldItemPosition] as TopMeal).id == (newList[newItemPosition] as TopMeal).id
//////                }
//////                is PopularMeal -> {
//////                    (oldList[oldItemPosition] as PopularMeal).name == (newList[newItemPosition] as PopularMeal).name
//////                }
//////            }
//////        }
//////
//////        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//////            return oldList[oldItemPosition] == newList[newItemPosition]
//////        }
//////    }
////
//////    class SectionsViewHolder(private val item: View) : RecyclerView.ViewHolder(item){
//////        val getScreenSize : IntArray = getScreenSize(item.context)
//////
//////        var menuItem: MenuMeals? = null
//////            set(value) {
//////                field = value
//////                value?.let {
//////                    itemView.menuName.text = value.name
//////                    val properWidth = if (item.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
//////                        getScreenSize[0]
//////                    }else{
//////                        getScreenSize[1]
//////                    }
//////
//////                    val bit = BitmapFactory.decodeResource(itemView.context.resources, value.icon)
//////                    val thumbImage: Bitmap
//////                    thumbImage = when(field.height.toFloat()){
//////                        itemView.context.resources.getDimension(R.dimen.menu_small_icon_height) -> {
//////                            ThumbnailUtils.extractThumbnail(bit, properWidth/2, value.height)
//////                        }
//////                        itemView.context.resources.getDimension(R.dimen.menu_middle_icon_height) -> {
//////                            ThumbnailUtils.extractThumbnail(bit, properWidth/2, value.height)
//////                        }
//////                        itemView.context.resources.getDimension(R.dimen.menu_long_icon_height) -> {
//////                            ThumbnailUtils.extractThumbnail(bit, properWidth, value.height)
//////                        }
//////                        else -> {
//////                            ThumbnailUtils.extractThumbnail(bit, properWidth/2, value.height)
//////                        }
//////                    }
//////
//////                    itemView.menuImage.setImageBitmap(thumbImage)
//////                    //                    Glide.with(itemView)
//////                    //                        .load(value.icon)
//////                    //                        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
//////                    //                        .centerCrop()
//////                    //                        .into(itemView.menuImage)
//////
//////                }
//////            }
//////    }
////
//////    class FooterViewHolder(private val item: View) : RecyclerView.ViewHolder(item){}
////
//////    class TitleViewHolder(private val item: View) : RecyclerView.ViewHolder(item){
//////        val recyclerView = RecyclerView
//////        init {
//////            itemView.findViewById(R.id.sub_recyclerviews)
//////        }
//////        var title: Title? = null
//////        set(value) {
//////            field = value
//////            value?.let {
//////                item.text_header_name.text = value.title
//////            }
//////        }
//////    }
//}
//
//
////class MenuAdapter(private val myDataset: Array<RecommendItems>) :
////    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
////
////    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)
////
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
////        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
////        return ViewHolder(itemView)
////    }
////
////    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
////        holder.item.findViewById<TextView>(R.id.itemName).text = (myDataset[position] as PopularMeal).name
////    }
////
////    override fun getItemCount() = myDataset.size
////
////    companion object {
////        const val USERNAME_KEY = "userName"
////    }
////}