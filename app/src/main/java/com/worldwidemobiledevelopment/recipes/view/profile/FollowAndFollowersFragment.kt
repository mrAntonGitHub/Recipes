package com.worldwidemobiledevelopment.recipes.view.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.adapters.FollowersAdapter
import com.worldwidemobiledevelopment.recipes.data.Follower
import com.worldwidemobiledevelopment.recipes.data.ResultStatus
import com.worldwidemobiledevelopment.recipes.utils.Utils
import kotlinx.android.synthetic.main.follow_and_followers_fragment.*
import kotlinx.android.synthetic.main.fragment_sign_up.toolbar

class FollowAndFollowersFragment : Fragment(), FollowersAdapter.FollowersAdapterAction {


    private lateinit var viewModel: FollowAndFollowersViewModel

    private lateinit var snakbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.follow_and_followers_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowAndFollowersViewModel::class.java)

        val container = arguments?.getString(CONTAINER_CODE)

        if (container == FOLLOWERS_CODE) {
            toolbar.title = "Подписчики"
            loadFollowersOrFollows(FOLLOWERS_CODE)

        } else {
            toolbar.title = "Подписки"
            loadFollowersOrFollows(FOLLOWS_CODE)
        }
        setupActionBar()

    }

    private fun loadFollowersOrFollows(code: String){
        lifecycleScope.launchWhenStarted {
            var list = mutableListOf<Follower>()
            when(code){
                FOLLOWERS_CODE -> {
                    val followers = viewModel.getUserFollowers()
                    when (followers.status) {
                        ResultStatus.SUCCESS -> {
                            list = followers.data?.toMutableList() ?: mutableListOf()
                            if (list.isNullOrEmpty()) {
                                showListIsEmpty(FOLLOWERS_CODE)
                            }
                        }
                        ResultStatus.ERROR -> { showError() }
                    }
                }
                FOLLOWS_CODE -> {
                    val follows = viewModel.getUserFollows()
                    when(follows.status){
                        ResultStatus.SUCCESS -> {
                            list = follows.data?.toMutableList() ?: mutableListOf()
                            if (list.isNullOrEmpty()) {
                                showListIsEmpty(FOLLOWS_CODE)
                            }
                        }
                        ResultStatus.ERROR -> { showError() }
                    }
                }
            }
            progress.visibility = View.GONE
            if (!list.isNullOrEmpty()){
                followersRv.adapter = FollowersAdapter(list, this@FollowAndFollowersFragment)
            }
        }
    }

    private fun showError(){
        progress.visibility = View.GONE
        snakbar = Utils.showSnackBarWithAction(followersRv,"Ошибка загрузки", anchorView = R.id.bottom_nav, actionText = "Перезагрузить") {
            Toast.makeText(requireActivity(), "Reloading...", Toast.LENGTH_SHORT).show()
        }
        snakbar.show()
    }

    override fun onStop() {
        if (::snakbar.isInitialized && snakbar.isShown) {
            snakbar.dismiss()
        }
        super.onStop()
    }

    private fun showListIsEmpty(code: String) {
        progress.visibility = View.GONE
        var text = ""
        when(code){
            FOLLOWERS_CODE -> {
                text = "У Вас пока нет подписчиков"
            }
            FOLLOWS_CODE -> {
                text = "Вы ни на кого не подписаны"
            }
        }
        emptyListMessage.text = text
        emptyListMessage.visibility = View.VISIBLE
    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun itemClicked(follower: Follower) {
        Toast.makeText(requireActivity(), follower.name, Toast.LENGTH_SHORT).show()
    }





}