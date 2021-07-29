package com.worldwidemobiledevelopment.recipes.view.imagePager

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.viewpager.widget.ViewPager
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.adapters.ViewPagerAdapter1
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.changeStatusBarColor
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.hideBottomNavigation
import com.worldwidemobiledevelopment.recipes.view.profile.newRecipe.CHOSEN_IMAGE_POSITION
import com.worldwidemobiledevelopment.recipes.view.profile.newRecipe.CODE_IMAGES_FROM_IMAGES_PAGER
import com.worldwidemobiledevelopment.recipes.view.profile.newRecipe.URIS_ARRAY
import kotlinx.android.synthetic.main.fragment_sign_up.toolbar
import kotlinx.android.synthetic.main.new_recipe_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field


class ImagePagerFragment : Fragment(R.layout.image_pager_fragment),
    ViewPagerAdapter1.ViewPagerAction1 {

    interface ImageViewPagerAction {
        fun removeImage(position: Int)
    }

    private lateinit var viewModel: ImagePagerViewModel

    lateinit var uris: MutableList<Uri>
    private var initiator = ""

    private var currentPage = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("ImagePagerFragment", "1")

        viewModel = ViewModelProvider(this).get(ImagePagerViewModel::class.java)

        hideBottomNavigation(requireActivity())

        changeStatusBarColor(requireActivity(), resources.getColor(android.R.color.black, null))

        lifecycleScope.launchWhenStarted {
            val urisArray = arguments?.getStringArrayList(URIS_ARRAY)
            val chosenPosition = arguments?.getInt(CHOSEN_IMAGE_POSITION)
            initiator = arguments?.getString("INITIATOR") ?: ""

            val uris = urisArray?.map { it.toUri() }


            if (!uris.isNullOrEmpty()) {
                this@ImagePagerFragment.uris = uris.toMutableList()
                chosenPosition?.let { setupActionBar(it, uris.count()) }
                currentPage = chosenPosition ?: -1
                CoroutineScope(IO).launch {
                    val adapter = ViewPagerAdapter1(uris, this@ImagePagerFragment)
                    val field: Field = ViewPager::class.java.getDeclaredField("mRestoredCurItem")
                    field.isAccessible = true
                    withContext(Main){
                        field.set(view_pager, chosenPosition)
                        view_pager.adapter = adapter
                    }
                }
            } else {
                Log.e("ImagePagerFragment", "URIs are empty")
            }
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.e("ImagePagerFragment", "Scrolled $position")
            }

            override fun onPageSelected(position: Int) {
                Log.e("ImagePagerFragment", "$position")
                currentPage = position
                (requireActivity() as AppCompatActivity).supportActionBar?.title =
                    "${position + 1} из ${this@ImagePagerFragment.uris.count()}"
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setupActionBar(position: Int, elements: Int) {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setTint(resources.getColor(R.color.white, null))
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            "${position + 1} из $elements"
    }

    private fun setChosenImages(uris: List<Uri>, initiator: String) {
        val arrayUris = arrayListOf<String>()
        arrayUris.addAll(uris.map { it.toString() })
        setFragmentResult(CODE_IMAGES_FROM_IMAGES_PAGER, bundleOf(initiator to arrayUris))
    }

    override fun clicked(position: Int) {

    }

    private fun deleteImage(position: Int) {
        if (uris.count() == 1) {
            uris.removeAt(position)
            setChosenImages(this.uris, initiator)
            requireActivity().onBackPressed()
        } else {
            uris.removeAt(position)
            val adapter = ViewPagerAdapter1(uris, this)
            val field: Field = ViewPager::class.java.getDeclaredField("mRestoredCurItem")
            field.isAccessible = true

            when (position) {
                0 -> {
                    field.set(view_pager, 0)
                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        "1 из ${uris.count()}"
                }
                uris.count() -> {
                    field.set(view_pager, uris.count() - 1)
                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        "$position из ${uris.count()}"
                }
                else -> {
                    field.set(view_pager, position)
                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        "$position из ${uris.count()}"
                }
            }
            view_pager.adapter = adapter
            setChosenImages(this.uris, initiator)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_image_page, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                Log.e("ImagePagerFragment", "$currentPage ${uris.count()}")
                if (uris.count() == 1) {
                    deleteImage(0)
                } else {
                    deleteImage(currentPage)
                }
                true
            }
            else -> {
                false
            }
        }
    }


}