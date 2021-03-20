package com.worldwidemobiledevelopment.recipes.view.home

import androidx.lifecycle.*
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeViewModel : ViewModel() {
    @Inject
    lateinit var repository: Repository

    private val _b = MutableLiveData<List<String>>()
    val b : LiveData<List<String>>
        get() = _b

//    var menuAdapter: MenuAdapter? = null

    init {
        Application.application.appComponent.inject(this)

        viewModelScope.launch {
            repository.getFoo()
                .collect {
                    _b.value = it
                }
        }
    }

      // Dimensions
//    var smallItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_small_icon_height)
//    var mediumItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_middle_icon_height)
//    var longItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_long_icon_height)
//    var menuItemsSpace = context.resources.getDimensionPixelSize(R.dimen.menu_items_spaces)



//    fun initMenu(){
//        /* Setup menu items with proper height */
//
//        smallItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_small_icon_height)
//        mediumItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_middle_icon_height)
//        longItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_long_icon_height)
//        menuItemsSpace = context.resources.getDimensionPixelSize(R.dimen.menu_items_spaces)
//
//        menuItemsList.value = listOf(
//            MenuItem("Закуски", R.drawable.ic_snacks, smallItemHeight),
//            MenuItem("Сэндвичи", R.drawable.ic_sandwich, smallItemHeight),
//            MenuItem("Салаты", R.drawable.ic_salad, longItemHeight),
//            MenuItem("Супы", R.drawable.ic_soup, smallItemHeight),
//            MenuItem("Выпечка", R.drawable.ic_bakery, smallItemHeight),
//            MenuItem("Основные блюда", R.drawable.ic_main_dish, longItemHeight),
//            MenuItem("Десерты", R.drawable.ic_desert, mediumItemHeight),
//            MenuItem("Напитки", R.drawable.ic_beverage, mediumItemHeight)
//        )
//    }
//
//    fun initFakePopularMeals(){
//            popularMealsList = listOf(
//                PopularMeal(1, "Закуски", R.drawable.ic_snacks),
//                PopularMeal(2, "Сэндвичи", R.drawable.ic_sandwich),
//                PopularMeal(3, "Салаты", R.drawable.ic_salad),
//                PopularMeal(4, "Супы", R.drawable.ic_soup),
//                PopularMeal(5, "Выпечка", R.drawable.ic_bakery),
//                PopularMeal(6,"Основные блюда", R.drawable.ic_main_dish),
//                PopularMeal(7, "Десерты", R.drawable.ic_desert),
//                PopularMeal(8, "Напитки", R.drawable.ic_beverage),
//            )
//    }
}