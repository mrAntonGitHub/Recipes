package com.worldwidemobiledevelopment.recipes.view.home

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.*
import com.worldwidemobiledevelopment.recipes.utils.decorators.CarouselItemDecoration
import com.worldwidemobiledevelopment.recipes.utils.decorators.InsetItemDecoration
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException

const val INSET_TYPE_KEY = "inset_type"
const val INSET = "inset"

class HomeFragment : Fragment(), CardItem.MealAction {

    lateinit var bottomSheet: BottomSheetDialog


    private lateinit var viewModel: HomeViewModel

    // Adapter for recyclerView
    lateinit var groupieAdapter: GroupieAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    private var gray: Int = 0
    private var betweenPadding: Int = 0

    // Updatable (can be sorted) section
    lateinit var popularMealsSection: Section

    lateinit var popularMeals: List<SmallCardItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        viewModel.b.observe(requireActivity()) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        setupActionBar()
        fetchJson()

        gray = ContextCompat.getColor(requireContext(), R.color.design_default_color_background)
        betweenPadding = resources.getDimensionPixelSize(R.dimen.padding_small)

        // Init Groupie
        groupieAdapter = GroupieAdapter()
        groupieAdapter.spanCount = 12
        gridLayoutManager = GridLayoutManager(requireContext(), groupieAdapter.spanCount)
        gridLayoutManager.spanSizeLookup = groupieAdapter.spanSizeLookup

        // Make sections
        makeDailyMealDay(groupieAdapter)
        makeCarouselSection(groupieAdapter)
        makeColumnSection(groupieAdapter)

        // Apply adapter to recyclerView
        rvHome.apply {
            this.layoutManager = gridLayoutManager
            addItemDecoration(HeaderItemDecoration(gray, betweenPadding))
            addItemDecoration(InsetItemDecoration(gray, betweenPadding))
            adapter = groupieAdapter
        }
    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(homeToolbar)
        setHasOptionsMenu(true)
    }

    private fun fetchJson() {
        val meatSection = getJsonFromAssets(requireContext(), "meat.json")
        val recipe: MealSection = Gson().fromJson(meatSection, MealSection::class.java)
        this.popularMeals = recipe.recipes.map {
            SmallCardItem(it, this)
        }

//        viewModel.verifyNumber(requireActivity(), "+79175046558")
//        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//        val map : MutableMap<String, Any> = HashMap()
//        map["tags"] = recipe.tags
//        map["category"] = recipe.name
//        map["recipes"] = recipe.recipes.toRecipeRight()


//        db.collection("Рецепты").document(recipe.name)
//            .set(map)


        // Отправка данных в  firebase!
//        CoroutineScope(IO).launch {
//            var reader: BufferedReader? = null;
//
//            reader = BufferedReader(InputStreamReader(requireContext().assets.open("ingredientsList.txt")))
//
//
//            val list: List<String>? = reader.readLines()
//
//            val testList = mutableListOf<String>()
//
//            for (i in 0..5){
//                list?.get(i)?.let { testList.add(it) }
//            }
//
//            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//            val map : MutableMap<String, Any> = HashMap()
//            testList.forEach {
//                map["posted_by"] = "От редакции"
//                db.collection("Ингридиенты").document(it)
//                    .set(map)
//            }
//
//            Log.e("sdfsdfsd", map.toString())
//
//
//
////            db.collection("Ингридиенты").document()
//
//            reader.close()
//        }


    }

//    fun List<Recipe>.toRecipeRight(): List<Recipe> {
//        val list = mutableListOf<Recipe>()
//        this.forEach {
//            val steps = mutableListOf<StepRight>()
//            it.steps.forEach {
//                steps.add(StepRight(it.description, it.stepTime, null))
//            }
//
//            //text = грамм:400,ккал:1012.0,белки:66.76,жиры:80.76,углеводы:0.0
//            val split = it.caloriesInfoPerServing.split("[:,]".toRegex())
//
//            list.add(
//                Recipe(
//                    it.name,
//                    it.description,
//                    steps,
//                    it.portionsNumber,
//                    it.additionalInfo,
//                    it.complexity,
//                    it.tags,
//                    it.ingredientsList,
//                    it.tagsDiet,
//                    it.cookingMinutes,
//                    split[1],
//                    split[3],
//                    split[5],
//                    split[7],
//                    split[9],
//                    null,
//                    0,
//                    User(),
//                    null, null, null
//                )
//            )
//        }
//        return list
//    }

    private fun getJsonFromAssets(context: Context, fileName: String?): String? {
        val jsonString: String
        jsonString = try {
            val inputStream = context.assets.open(fileName!!)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun makeDailyMealDay(groupieAdapter: GroupieAdapter) {
        val fullBleedItemSection = Section(HeaderItem(R.string.day_meal))
        fullBleedItemSection.add(
            FullBleedCardItem(
                Meal(
                    "1",
                    "Лосось по царски",
                    R.drawable.ic_main_dish
                ), this
            )
        )
        groupieAdapter.add(fullBleedItemSection)
    }

    private fun makeCarouselSection(groupieAdapter: GroupieAdapter) {
        val carouselSection = Section(HeaderItem(R.string.recommend))
        carouselSection.setHideWhenEmpty(true)
        val carousel = makeCarouselGroup()
        carouselSection.add(carousel)
        groupieAdapter.add(carouselSection)
    }

    private fun makeCarouselGroup(): Group {
        val carouselDecoration = CarouselItemDecoration(gray, betweenPadding)
        val carouselAdapter = GroupieAdapter()

        carouselAdapter.addAll(carouselList)
        return CarouselGroup(carouselDecoration, carouselAdapter)
    }

    private fun makeColumnSection(groupieAdapter: GroupieAdapter) {
        this.popularMealsSection =
            Section(HeaderItem(R.string.popular, iconResId = R.drawable.ic_sort)
            {
                Toast.makeText(requireActivity(), "Sort Clicked!", Toast.LENGTH_SHORT).show()
                popularMealsSection.update(popularMeals.reversed())
                bottomSheet = BottomSheetDialog(requireContext(), R.style.Theme_BottomSheet)
                val view = LayoutInflater.from(requireContext()).inflate(
                    R.layout.sheet_sorting,
                    requireActivity().findViewById(R.id.sortingSheet)
                )

                bottomSheet.setContentView(view)
                bottomSheet.show()

                bottomSheet.findViewById<TextView>(R.id.tvSortRatingUp)?.setOnClickListener {
                    Toast.makeText(requireContext(), "UP", Toast.LENGTH_SHORT).show()
                    bottomSheet.dismissWithAnimation = true
                    bottomSheet.dismiss()
                }
                bottomSheet.findViewById<TextView>(R.id.tvSortRatingDown)?.setOnClickListener {
                    Toast.makeText(requireContext(), "DOWN", Toast.LENGTH_SHORT).show()
                    bottomSheet.dismiss()
                }

                // You can also do this by forcing a change with payload
                rvHome.post { rvHome.invalidateItemDecorations() }
            }
            )
        popularMealsSection.addAll(popularMeals)
        groupieAdapter.add(popularMealsSection)
    }

    override fun mealClicked(recipe: Recipe) {
        val bundle = bundleOf("name" to recipe.name)
        view?.findNavController()?.navigate(R.id.action_homeFragment_to_mealFragment, bundle)
    }

    override fun mealLiked(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun addToBookmarkClicked(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    private val carouselList = listOf(
        CarouselCardItem(Meal("1", "Что-то", R.drawable.ic_snacks), this),
        CarouselCardItem(Meal("2", "Что-то", R.drawable.ic_snacks), this),
        CarouselCardItem(Meal("3", "Что-то", R.drawable.ic_snacks), this),
        CarouselCardItem(Meal("4", "Что-то", R.drawable.ic_snacks), this),
        CarouselCardItem(Meal("5", "Что-то", R.drawable.ic_snacks), this),
        CarouselCardItem(Meal("6", "Что-то", R.drawable.ic_snacks), this),
        CarouselCardItem(Meal("7", "Что-то", R.drawable.ic_snacks), this),
    )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }


}


//private val listOfAvatars = listOf(
//    Title("Закуска"),
//    Title("Сэндвичи"),Title("Закуска"),
//    Title("Сэндвичи"),Title("Закуска"),
//    Title("Сэндвичи"),Title("Закуска"),
//    Title("Сэндвичи"),Title("Закуска"),
//    Title("Сэндвичи"),Title("Закуска"),
//    Title("Сэндвичи"),Title("Закуска"),
//    Title("Сэндвичи"),Title("Закуска"),
//    MenuItem("Закуски", R.drawable.ic_snacks, 165),
//    MenuItem("Сэндвичи", R.drawable.ic_sandwich, 165),
//    MenuItem("Салаты", R.drawable.ic_salad, 165),
//    MenuItem("Супы", R.drawable.ic_soup, 165),
//    MenuItem("Выпечка", R.drawable.ic_bakery, 165),
//    MenuItem("Основные блюда", R.drawable.ic_main_dish, 165),
//    MenuItem("Десерты", R.drawable.ic_desert, 165),
//    MenuItem("Напитки", R.drawable.ic_beverage, 165),
//    MenuItem("Закуски", R.drawable.ic_snacks, 165)
//)









