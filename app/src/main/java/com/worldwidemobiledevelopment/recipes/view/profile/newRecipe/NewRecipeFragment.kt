package com.worldwidemobiledevelopment.recipes.view.profile.newRecipe

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.adapters.*
import com.worldwidemobiledevelopment.recipes.data.CookingStep
import com.worldwidemobiledevelopment.recipes.data.Ingredient
import com.worldwidemobiledevelopment.recipes.data.Recipe
import com.worldwidemobiledevelopment.recipes.utils.Utils
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.changeStatusBarColor
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.hideBottomNavigation
import kotlinx.android.synthetic.main.new_recipe_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val URIS_ARRAY = "URIS_ARRAY"
const val CHOSEN_IMAGE_POSITION = "CHOSEN_IMAGE_POSITION"

// Codes for BottomSheet image picker
const val REQUEST_CODE_RECIPE_IMAGE = "REQUEST_CODE_RECIPE_IMAGE"
const val REQUEST_CODE_STEP_IMAGE = "REQUEST_CODE_STEP_IMAGE"

const val CODE_PROFILE_BUNDLE = "CODE_PROFILE_BUNDLE"
const val CODE_EDIT_RECIPE_ID = "CODE_EDIT_RECIPE_ID"

const val CODE_IMAGES_FROM_IMAGES_PAGER = "CODE_IMAGES_FROM_IMAGES_PAGER"
const val IMAGES_PAGER_RECIPE_IMAGES = "IMAGES_PAGER_RECIPE_IMAGES"
const val IMAGES_PAGER_STEP_IMAGES = "IMAGES_PAGER_STEP_IMAGES"

const val MAX_IMAGES_TO_SELECT = 4

class NewRecipeFragment : Fragment(R.layout.new_recipe_fragment),
    BottomSheetImagePicker.OnImagesSelectedListener, StepAdapterDelegate, ViewPagerAdapterDelegate,
    IngredientAdapterDelegate {

    private var stepToAddImage: Int? = null

    private lateinit var viewModel: NewRecipeViewModel

    private val ingredientsAdapter = IngredientAdapter()
    private val stepAdapter = StepAdapter()

    private var maxRecipeImages = MAX_IMAGES_TO_SELECT
    private var maxStepImages = MAX_IMAGES_TO_SELECT
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewRecipeViewModel::class.java)

        hideBottomNavigation(requireActivity())
        changeStatusBarColor(requireActivity(), getColor(resources, R.color.bg, null))
        setupActionBar()
        val recipeToEditId = arguments?.getBundle(CODE_PROFILE_BUNDLE)?.getString(
            CODE_EDIT_RECIPE_ID
        )

        if (!recipeToEditId.isNullOrEmpty()) {
            loadRecipeToEdit()
        } else {
            loadNewRecipe()
        }
        setupClickListeners()
    }

    private fun loadRecipeToEdit() {
        // TODO load recipe to edit
    }

    private fun loadNewRecipe() {
        loadRecipeImages()

        val list = viewModel.steps
        stepAdapter.attachDelegate(this)
        stepAdapter.submitList(list)
        stepsRv.adapter = stepAdapter

        ingredientsAdapter.attachDelegate(this)
        ingredientsAdapter.submitList(viewModel.ingredients)
        ingredientsRv.adapter = ingredientsAdapter
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun loadRecipeImages() {
        setFragmentResultListener(CODE_IMAGES_FROM_IMAGES_PAGER) { _, bundle ->
            val urisStringArray = bundle.getStringArrayList(IMAGES_PAGER_RECIPE_IMAGES)
            val urisArray = urisStringArray?.map { it.toUri() }
            if (urisArray != null) {
                viewModel.chosenUris = urisArray.toMutableList()
                setRecipeImages(viewModel.chosenUris)

            }

            val urisStepStringArray = bundle.getStringArrayList(IMAGES_PAGER_STEP_IMAGES)
            val urisStepArray = urisStepStringArray?.map { it.toUri() }
            val stepPosition = stepToAddImage
            if (urisStepArray != null && stepPosition != null) {
                viewModel.steps[stepPosition].imageUrlSet =
                    urisStepArray.map { it.toString() }.toMutableList()
                val list = viewModel.steps
                stepAdapter.submitList(list)
                stepToAddImage = null
            }
        }
        setRecipeImages(viewModel.chosenUris)
    }


    private fun setupClickListeners() {

        addPhoto.setOnClickListener {
            pickRecipeImages()
        }

        addPhotoBtn.setOnClickListener {
            pickRecipeImages()
        }

        initComplexityLevels()

    }

    private fun initComplexityLevels() {
        easy.setOnClickListener {
            easy.isSelected = true
            medium.isSelected = false
            hard.isSelected = false
        }
        medium.setOnClickListener {
            easy.isSelected = false
            medium.isSelected = true
            hard.isSelected = false
        }
        hard.setOnClickListener {
            easy.isSelected = false
            medium.isSelected = false
            hard.isSelected = true
        }
    }


    private fun pickRecipeImages() {
        BottomSheetImagePicker.Builder(getString(R.string.file_provider))
            .multiSelect(1, maxRecipeImages)
            .multiSelectTitles(
                R.plurals.pick_multi,
                R.plurals.pick_multi,
                R.string.pick_multi_limit
            )
            .peekHeight(R.dimen.bottom_sheet_image_pick_peek_height)
            .columnSize(R.dimen.bottom_sheet_image_pick_column_size)
            .requestTag(REQUEST_CODE_RECIPE_IMAGE)
            .show(childFragmentManager)
    }

    private fun pickStepImage() {
        BottomSheetImagePicker.Builder(getString(R.string.file_provider))
            .multiSelect(1, maxStepImages)
            .multiSelectTitles(
                R.plurals.pick_multi,
                R.plurals.pick_multi,
                R.string.pick_multi_limit
            )
            .peekHeight(R.dimen.bottom_sheet_image_pick_peek_height)
            .columnSize(R.dimen.bottom_sheet_image_pick_column_size)
            .requestTag(REQUEST_CODE_STEP_IMAGE)
            .show(childFragmentManager)
    }

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        lifecycleScope.launchWhenStarted {
            when (tag) {
                REQUEST_CODE_RECIPE_IMAGE -> {
                    viewModel.chosenUris.addAll(uris.toMutableList())
                    setRecipeImages(viewModel.chosenUris)
                }
                REQUEST_CODE_STEP_IMAGE -> {
                    val addToPosition = stepToAddImage
                    if (addToPosition != null) {
                        viewModel.steps[addToPosition].imageUrlSet?.addAll(uris.map { it.toString() })
                        val list = viewModel.steps
                        stepAdapter.submitList(list)
                        stepAdapter.notifyItemChanged(addToPosition)
                        stepToAddImage = null
                    }

                }
            }
        }
    }

    private fun setRecipeImages(uris: List<Uri>?) {
        CoroutineScope(IO).launch {
            if (!uris.isNullOrEmpty()) {
                withContext(Main) {
                    image.visibility = View.GONE
                    recipeProgress.visibility = View.VISIBLE

                    addPhoto.isEnabled = false
                    addPhotoBtn.text = "Добавить фото"
                    maxRecipeImages = MAX_IMAGES_TO_SELECT - uris.size
                }

                val pagerAdapter = ImageViewPagerAdapter(uris, this@NewRecipeFragment)
                val tabLayout = requireActivity().findViewById(R.id.tab_layout) as TabLayout
                withContext(Main) {
                    tab_layout.visibility = View.VISIBLE
                    view_pager.visibility = View.VISIBLE
                    recipeProgress.visibility = View.GONE
                    view_pager.adapter = pagerAdapter
                    tabLayout.setupWithViewPager(view_pager, true)
                }

            } else {
                addPhoto.isEnabled = true
                image.visibility = View.VISIBLE
                tab_layout.visibility = View.GONE
                view_pager.visibility = View.GONE
                maxRecipeImages = MAX_IMAGES_TO_SELECT
            }
        }
    }


    private fun openImageFragment(uris: List<Uri>, chosenPosition: Int, initiator: String) {
        Log.e("ImagePagerFragment", "-1")
        // Open image set in separate fragment
        val arrayUris = arrayListOf<String>()
        arrayUris.addAll(uris.map { it.toString() })
        val bundle = bundleOf(
            URIS_ARRAY to arrayUris,
            CHOSEN_IMAGE_POSITION to chosenPosition,
            "INITIATOR" to initiator
        )
        findNavController().navigate(R.id.action_newRecipeFragment_to_imagePagerFragment, bundle)
        Log.e("ImagePagerFragment", "0")
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::viewModel.isInitialized) {
            viewModel.ingredients = ingredientsAdapter.getIngredientsList().toMutableList()
            viewModel.steps = stepAdapter.getStepsList().toMutableList()
        }
    }

    override fun onDetach() {
        super.onDetach()
        val bn = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bn.visibility = View.VISIBLE
    }


    override fun imageClicked(position: Int) {
        openImageFragment(viewModel.chosenUris, position, IMAGES_PAGER_RECIPE_IMAGES)
    }

    override fun imageLongClicked(position: Int) {
        AlertDialog.Builder(requireActivity())
            .setMessage("Удалить изображение?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.chosenUris.removeAt(position)
                setRecipeImages(viewModel.chosenUris)
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_recipe, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveRecipeAttempt()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun saveRecipeAttempt() {
        if (isAllFieldsExist()) {
            AlertDialog.Builder(requireActivity())
                .setMessage("Опубликовать рецепт?")
                .setPositiveButton("Да") { _, _ ->
                    saveRecipe()
                }
                .setNegativeButton("Отмена", null)
                .create()
                .show()
        }
    }

    private fun isAllFieldsExist(): Boolean {
        val recipeImages = view_pager.adapter
        if (recipeImages != null && recipeImages.count > 0) {
            val name = name.text
            if (name.trim().isNotEmpty()) {
                val cookingTime = cookingTime.text
                if (cookingTime.trim().isNotEmpty()) {
                    if (viewModel.ingredients.size > 0) {
                        if (viewModel.steps.size > 0) {
                            if (easy.isSelected || medium.isSelected || hard.isSelected) {
                                return true
                            } else {
                                Utils.showSnackBar(view_pager, "Укажите сложноть приготовления")
                                return false
                            }
                        } else {
                            Utils.showSnackBar(view_pager, "Добавьте шаги приготовления")
                            return false
                        }
                    } else {
                        Utils.showSnackBar(view_pager, "Добавьте ингридиенты")
                        return false
                    }
                } else {
                    Utils.showSnackBar(view_pager, "Укажите время готовки")
                    return false
                }
            } else {
                Utils.showSnackBar(view_pager, "Укажите название блюда")
                return false
            }
        } else {
            Utils.showSnackBar(view_pager, "Добавьте фото блюда")
            return false
        }
    }

    private fun saveRecipe() {
        val name = name.text.toString()
        val description = description.text.toString()
        val cookingTime = cookingTime.text.toString()
        val portionsNumber = portionsNumber.text.toString()
        val complexity: Int =
            if (easy.isSelected) 1 else if (medium.isSelected) 2 else if (hard.isSelected) 3 else -1

        val additionalInfo = additionalInfo.text.toString()
        val kcal = kcal.text.toString()
        val proteins = proteins.text.toString()
        val fats = fats.text.toString()
        val carbohydrates = carbohydrates.text.toString()

        val recipe = Recipe(
            "",
            null,
            name,
            viewModel.chosenUris.map { it.toString() },
            description,
            viewModel.steps,
            portionsNumber,
            additionalInfo,
            complexity,
            null,
            viewModel.ingredients,
            null,
            cookingTime,
            null,
            kcal,
            proteins,
            fats,
            carbohydrates,
            null,
            null,
            null
        )
        viewModel.publishRecipe(recipe)
    }

    override fun removeStep(position: Int) {
        val list = stepAdapter.getStepsList().toMutableList()
        try {
            list.removeAt(position)
        } catch (e: Exception) {
            Log.d(
                "NewRecipeFragment",
                "Deleting so fast: $e"
            )
        }
        viewModel.steps = list
        stepAdapter.submitList(viewModel.steps)
    }

    override fun addStep() {
        viewModel.steps = stepAdapter.getStepsList().toMutableList()
        viewModel.steps.add(CookingStep(""))
        val list = viewModel.steps
        stepAdapter.submitList(list)
    }

    override fun addStepImages(position: Int) {
        stepToAddImage = position
        pickStepImage()
    }

    override fun onImageClicked(stepPosition: Int, imagePosition: Int) {
        stepToAddImage = stepPosition
        val uris = viewModel.steps[stepPosition].imageUrlSet?.map { it }
        Log.e("NewRecipeFragment", "${uris?.size}")
        uris?.let { openImageFragment(it.map { it.toUri() }, imagePosition, IMAGES_PAGER_STEP_IMAGES) }
    }

    override fun removeIngredient(position: Int) {
        val list = ingredientsAdapter.getIngredientsList().toMutableList()
        try {
            list.removeAt(position)
        } catch (e: Exception) {
            Log.d(
                "NewRecipeFragment",
                "Deleting so fast: $e"
            )
        }
        viewModel.ingredients = list
        ingredientsAdapter.submitList(viewModel.ingredients)
    }

    override fun addIngredient() {
        viewModel.ingredients = ingredientsAdapter.getIngredientsList().toMutableList()
        viewModel.ingredients.add(Ingredient("" to ""))
        ingredientsAdapter.submitList(viewModel.ingredients)
    }


}