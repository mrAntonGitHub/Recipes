package com.worldwidemobiledevelopment.recipes.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.adapters.MyRecipeAdapter
import com.worldwidemobiledevelopment.recipes.data.Recipe
import kotlinx.android.synthetic.main.fragment_my_recipes.*
import javax.inject.Inject


class MyRecipesFragment : Fragment(R.layout.fragment_my_recipes),
    MyRecipeAdapter.MyRecipeAdapterAction {

    @Inject lateinit var viewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Application.application.appComponent.inject(this)

    }

    override fun onResume() {
        super.onResume()
        myRecipesRv.setHasFixedSize(true)
        myRecipesRv.adapter = MyRecipeAdapter(viewModel.recipes, this)
    }

    override fun itemClicked(recipe: Recipe) {
        Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show()
    }

}