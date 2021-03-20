package com.worldwidemobiledevelopment.recipes.view.meal

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.adapters.CommentsAdapter
import com.worldwidemobiledevelopment.recipes.adapters.CookingStepsAdapter
import com.worldwidemobiledevelopment.recipes.adapters.IngredientsAdapter
import com.worldwidemobiledevelopment.recipes.data.Comment
import com.worldwidemobiledevelopment.recipes.data.CookingStep
import kotlinx.android.synthetic.main.fragment_meal.*
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView

class MealFragment : Fragment(), IngredientsAdapter.IngredientsAction,
    CookingStepsAdapter.StepAction, CommentsAdapter.CommentAction {

    lateinit var viewModel: MealViewModel

    private lateinit var flipperLayout: FlipperLayout

    private var hasComment = false

    private var currentStars = 0

    private var sentStars : Int? = null
    private var sentComment = ""

    lateinit var stars : List<ImageView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        flipperLayout = requireActivity().findViewById(R.id.flipperLayout)

        stars = listOf(firstStar, secondStar, thirdStar, fourthStar, fifthStar)

        setupActionBar()
        setupImagesSet()
        setupIngredients(viewModel.ingredientsList)
        setupCookingSteps(viewModel.cookingSteps)
        setupComments(viewModel.commetns)



        userContainer.setOnClickListener {
            Toast.makeText(requireContext(), "User clicked", Toast.LENGTH_SHORT).show()
        }


        ingredients.setOnClickListener {
            Toast.makeText(requireContext(), "Cart clicked", Toast.LENGTH_SHORT).show()
        }

        setOnRateListener()

        writeComment.setOnClickListener {
            if (writeComment.text == "Отмена"){
                cancelComment()
            }else if (hasComment){
                editComment()
            }else{
                addComment()
            }
        }

        sendComment.setOnClickListener {
            sentStars = currentStars
            sentComment = comment.text.toString()
            if (comment.text.trim().isEmpty()){
                comment.visibility = View.GONE
            }
            comment.isEnabled = false
            setViewEnable(false, stars)
            writeComment.text = "Изменить"
            hasComment = true
            sendComment.visibility = View.GONE
        }

    }

    private fun addComment(){
        comment.isEnabled = true
        setViewEnable(true, stars)
        setVisibility(true, comment, sendComment)
        writeComment.text = "Отмена"
    }

    private fun editComment(){
        if (comment.isEnabled){
            if (comment.text.trim().isNotEmpty()){
                comment.visibility = View.VISIBLE
                comment.setText(sentComment)
            }
            setSelected(true, stars)
            sendComment.visibility = View.GONE
            setViewEnable(false, stars)
            writeComment.text = "Изменить"
        }else{
            comment.visibility = View.VISIBLE
            comment.isEnabled = true
            sendComment.visibility = View.VISIBLE
            setViewEnable(true, stars)
            writeComment.text = "Отмена"
        }
    }

    private fun cancelComment(){
        sendComment.visibility = View.GONE
        if (hasComment){
            if (comment.text.trim().isNotEmpty()){
                comment.visibility = View.VISIBLE
                comment.setText(sentComment)
                comment.isEnabled = false
            }else{
                comment.visibility = View.GONE
            }
            onStarClicked(sentStars)
            setViewEnable(false, stars)
            writeComment.text = "Изменить"
        }else{
            setVisibility(false, comment)
            setSelected(false, stars)
            comment.text.clear()
            writeComment.text = "Напишите отзыв"
        }
        comment.isEnabled = false
    }

    private fun setVisibility(isVisible: Boolean, vararg view: View){
        if (isVisible){
            view.forEach { it.visibility = View.VISIBLE }
        }else{
            view.forEach { it.visibility = View.GONE }
        }
    }

    private fun setOnRateListener() {
        firstStar.setOnClickListener { onStarClicked(1) }
        secondStar.setOnClickListener { onStarClicked(2) }
        thirdStar.setOnClickListener { onStarClicked(3) }
        fourthStar.setOnClickListener { onStarClicked(4) }
        fifthStar.setOnClickListener { onStarClicked(5) }
    }

    fun onStarClicked(starNumber: Int?) {
        addComment()
        when (starNumber) {
            1 -> {
                firstStar.isSelected = true
                currentStars = 1
                setSelected(false, secondStar, thirdStar, fourthStar, fifthStar)
            }
            2 -> {
                currentStars = 2
                setSelected(true, firstStar, secondStar)
                setSelected(false, thirdStar, fourthStar, fifthStar)
            }
            3 -> {
                currentStars = 3
                setSelected(true, firstStar, secondStar, thirdStar)
                setSelected(false, fourthStar, fifthStar)
            }
            4 -> {
                currentStars = 4
                setSelected(true, firstStar, secondStar, thirdStar, fourthStar,)
                fifthStar.isSelected = false
            }
            5 -> {
                currentStars = 5
                setSelected(true, firstStar, secondStar, thirdStar, fourthStar, fifthStar)
            }
            null -> {
                currentStars = 0
            }
        }

    }

    private fun setSelected(isSelected: Boolean, vararg view: View){
        view.forEach {
            it.isSelected = isSelected
        }
    }

    private fun setSelected(isSelected: Boolean, views: List<View>){
        views.forEach {
            it.isSelected = isSelected
        }
    }

    private fun setViewEnable(isEnabled: Boolean, view: List<View>){
        view.forEach {
            it.isClickable = isEnabled
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // TODO save data to VM on configuration changed
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_meal, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_to_bookmark -> {
                Toast.makeText(requireContext(), "addToBookmark clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share -> {
                Toast.makeText(requireContext(), "share clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                Toast.makeText(requireContext(), "ELSE", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    private fun setupIngredients(ingredients: List<Pair<String, String>>) {
        ingredientsRv.adapter = IngredientsAdapter(ingredients, this)
    }

    private fun setupCookingSteps(cookingSteps: List<CookingStep>) {
        stepsRv.adapter = CookingStepsAdapter(cookingSteps, this)
    }

    private fun setupComments(comments: List<Comment>) {
        commentsRv.adapter = CommentsAdapter(comments, this)
    }

    override fun onResume() {
        super.onResume()
        setupStatusBar()
    }

    private fun setupStatusBar() {
        // Make status bar transparent
        val window = requireActivity().window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun setupImagesSet() {
        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in viewModel.mealImages.indices) {
            val view = FlipperView(requireContext())
                .setDescriptionBackgroundColor(Color.TRANSPARENT)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setImage(viewModel.mealImages[i]) { flipperImageView, image ->
                    Picasso.get().load(image as String).into(flipperImageView)
                }
            view.setOnFlipperClickListener(object : FlipperView.OnFlipperClickListener {
                override fun onFlipperClick(flipperView: FlipperView) {

                }
            })
            flipperViewList.add(view)
        }

        flipperLayout.addFlipperViewList(flipperViewList)
        flipperLayout.showInnerPagerIndicator()
        flipperLayout.setCircleIndicatorHeight(20)
        flipperLayout.setIndicatorBackground(android.R.color.transparent)
    }


    override fun onStop() {
        super.onStop()
        val w = requireActivity().window
        w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun addToCart(ingredient: Pair<String, String>) {
        TODO("Not yet implemented")
    }

    override fun onImageClicked(step: CookingStep) {
        TODO("Not yet implemented")
    }

    override fun onCommentClicked(step: CookingStep) {
        TODO("Not yet implemented")
    }

}