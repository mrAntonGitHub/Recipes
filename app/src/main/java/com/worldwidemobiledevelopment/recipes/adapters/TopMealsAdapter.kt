//package com.worldwidemobiledevelopment.recipes.adapters
//
//import android.content.res.Configuration
//import android.graphics.BitmapFactory
//import android.media.ThumbnailUtils
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.worldwidemobiledevelopment.recipes.R
//import com.worldwidemobiledevelopment.recipes.data.PopularMeal
//import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.getScreenSize
//import kotlinx.android.synthetic.main.item_popular_meal.view.*
//
//class TopMealsAdapter : RecyclerView.Adapter<TopMealsAdapter.ViewHolder>() {
//
//    private var items: List<PopularMeal> = listOf()
////    private var itemDelegate : ItemDelegate? = null
//
//    override fun getItemCount(): Int = items.count()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Log.e("asdasdasdasd", "${holder.getScreenSize[0]}")
//        holder.itemView.layoutParams.width = holder.itemView.resources.getDimension(R.dimen.menu_small_icon_width).toInt()
//        holder.itemView.layoutParams.height = holder.itemView.resources.getDimension(R.dimen.menu_small_icon_height).toInt()
//        holder.popularMeal = items[position]
//    }
//
////    fun attachDelegate(itemDelegate: ItemDelegate){
////        this.itemDelegate = itemDelegate
////    }
//
//    fun submitList(items: List<PopularMeal>) {
//        val diffResult: DiffUtil.DiffResult =
//            DiffUtil.calculateDiff(ItemsDiffCallback(this.items, items))
//        this.items = items
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    class ItemsDiffCallback(var oldList: List<PopularMeal>, var newList: List<PopularMeal>) :
//        DiffUtil.Callback() {
//        override fun getOldListSize(): Int = oldList.size
//
//        override fun getNewListSize(): Int = newList.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return (oldList[oldItemPosition].id == newList[newItemPosition].id)
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldList[oldItemPosition] == newList[newItemPosition]
//        }
//    }
//
//    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
//
//        val getScreenSize : IntArray = getScreenSize(item.context)
//
//        var popularMeal: PopularMeal? = null
//            set(value) {
//                field = value
//                value?.let {
//                    itemView.itemName.text = value.name
//                    val properWidth = if (item.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
//                        getScreenSize[0]
//                    }else{
//                        getScreenSize[1]
//                    }
//
//                    val bit = BitmapFactory.decodeResource(itemView.context.resources, value.icon)
//                    val thumbImage = ThumbnailUtils.extractThumbnail(bit, properWidth/2, itemView.resources.getDimension(R.dimen.menu_small_icon_height).toInt())
//
////                    itemView.itemIcon.setImageBitmap(thumbImage)
//                }
//            }
//    }
//
//
//}