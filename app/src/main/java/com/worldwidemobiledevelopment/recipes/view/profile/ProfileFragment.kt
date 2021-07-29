package com.worldwidemobiledevelopment.recipes.view.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.auth.User
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.adapters.SectionsPagerAdapter
import com.worldwidemobiledevelopment.recipes.utils.Utils
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.changeStatusBarColor
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.stringFromTemplate
import kotlinx.android.synthetic.main.fragment_meal.*
import kotlinx.android.synthetic.main.fragment_profile.*

const val CONTAINER_CODE = "ContainerCode"
const val FOLLOWERS_CODE = "1000"
const val FOLLOWS_CODE = "1001"

class ProfileFragment : Fragment(R.layout.fragment_profile), View.OnClickListener, BottomSheetImagePicker.OnImagesSelectedListener{

    private lateinit var viewModel: ProfileViewModel

    lateinit var levelInfoDialogFragment: LevelInfoDialogFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        changeStatusBarColor(requireActivity(), resources.getColor(android.R.color.white, null))

        Application.application.appComponent.inject(this)

        setupSections()
        setupClicks()
        loadUser()
    }

    private fun setupSections() {
        // Load "MeRecipes" and "LikedRecipes" sections (Fragments)
        val adapter = SectionsPagerAdapter(requireActivity(), childFragmentManager)
        tabs.setupWithViewPager(view_pager)
        view_pager.adapter = adapter
    }

    private fun loadUser(){
        lifecycleScope.launchWhenCreated {
            val user = viewModel.getUser()
            if (user != null){
                Glide
                    .with(this@ProfileFragment)
                    .load(user.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(avatar)

                val userLevel = viewModel.getCurrentAndNextLevel(user.progress)
                userLevel.apply {
                    level.text = first.first
                    currentLevel.text = first.first
                    nextLevel.text = first.second
                    progressValue.text =  String.format(resources.getString(R.string.progress_value), user.progress - userLevel.second.first, userLevel.second.second)
                    progress.max = second.second
                    progress.progress = user.progress - userLevel.second.first

                    levelInfoDialogFragment = LevelInfoDialogFragment(first.first, first.second, user.progress - userLevel.second.first, second.second)
                }



                name.text = user.name

                val recipesCount = user.recipes?.count() ?: 0
                recipesValue.text = "$recipesCount"
                recipesText.text = requireActivity().resources.getQuantityString(R.plurals.recipes, recipesCount)

                val followersCount = user.followers?.count() ?: 0
                followersValue.text = "$followersCount"
                followersText.text = requireActivity().resources.getQuantityString(R.plurals.followers, followersCount)

                val followsCount = user.follows?.count() ?: 0
                followValue.text = "$followsCount"
                followText.text = requireActivity().resources.getQuantityString(R.plurals.followings, followsCount)
            }
        }
    }

    private fun setupClicks() {
        avatar.setOnClickListener { pickImage() }

        followers.setOnClickListener {
            val bundle = bundleOf(CONTAINER_CODE to FOLLOWERS_CODE)
            findNavController().navigate(R.id.action_profileFragment_to_followAndFollowersFragment, bundle)
        }

        follows.setOnClickListener {
            val bundle = bundleOf(CONTAINER_CODE to FOLLOWS_CODE)
            findNavController().navigate(R.id.action_profileFragment_to_followAndFollowersFragment, bundle)
        }

        level.setOnClickListener(this)
        levelDescription.setOnClickListener(this)
        progressValue.setOnClickListener(this)
        progress.setOnClickListener(this)
        currentLevel.setOnClickListener(this)
        nextLevel.setOnClickListener(this)

        settings.setOnClickListener {
            Toast.makeText(requireActivity(), "Setting", Toast.LENGTH_SHORT).show()
        }
        notifications.setOnClickListener {
            Toast.makeText(requireActivity(), "Notifications", Toast.LENGTH_SHORT).show()
        }

        addRecipe.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_newRecipeFragment)
        }
    }

    override fun onClick(view: View?) {
        if (::levelInfoDialogFragment.isInitialized){
            levelInfoDialogFragment.show(requireActivity().supportFragmentManager, "LEVEL INFO DIALOG")
        }else{ Log.e("ProfileFragment", "Can't show Level Dialog, levelInfoDialogFragment is not Initialized")}
    }

    private fun pickImage(){
        BottomSheetImagePicker.Builder(requireActivity().resources.getString(R.string.file_provider))
            .cameraButton(ButtonType.Button)
            .galleryButton(ButtonType.Button)
            .singleSelectTitle(R.string.bottom_sheet_dialog_pick_image)
            .peekHeight(R.dimen.bottom_sheet_image_pick_peek_height)
            .columnSize(R.dimen.bottom_sheet_image_pick_column_size)
            .requestTag("single")
            .show(childFragmentManager)
    }

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        lifecycleScope.launchWhenStarted {
            viewModel.setAvatar(uris[0])
            Glide
                .with(this@ProfileFragment)
                .load(uris[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar)
            Utils.showSnackBar(view = avatar, text = "Фото обновлено", duration = Snackbar.LENGTH_SHORT)
        }
    }


}