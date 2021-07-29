package com.worldwidemobiledevelopment.recipes.view.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.adapters.BookedRecipesAdapter
import com.worldwidemobiledevelopment.recipes.adapters.MyRecipeAdapter
import com.worldwidemobiledevelopment.recipes.adapters.RecyclerItemDecoration
import com.worldwidemobiledevelopment.recipes.data.Recipe
import com.worldwidemobiledevelopment.recipes.utils.Utils
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.toDp
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.toPx
import com.worldwidemobiledevelopment.recipes.utils.decorators.RightSpaceDecorator
import kotlinx.android.synthetic.main.fragment_booked_recipes.*
import kotlinx.android.synthetic.main.fragment_my_recipes.*
import javax.inject.Inject

class BookedRecipesFragment : Fragment(R.layout.fragment_booked_recipes),
    BookedRecipesAdapter.BookedRecipeAdapterAction {

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Application.application.appComponent.inject(this)

        bookedRecipesRv.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        bookedRecipesRv.adapter = BookedRecipesAdapter(viewModel.bookedRecipes, this)
    }

    override fun itemClicked(recipe: Recipe) {
        TODO("Not yet implemented")
    }

}