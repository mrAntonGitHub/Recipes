package com.worldwidemobiledevelopment.recipes.view.profile

import androidx.lifecycle.ViewModel
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.Follower
import com.worldwidemobiledevelopment.recipes.data.ResultStatus
import com.worldwidemobiledevelopment.recipes.data.ResultWrapper
import com.worldwidemobiledevelopment.recipes.repository.IRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FollowAndFollowersViewModel : ViewModel() {

    @Inject lateinit var repository: IRepository

    init {
        Application.application.appComponent.inject(this)
    }

    suspend fun getUserFollowers(): ResultWrapper<List<Follower>> {
        // Return List of followers
        val user = repository.getUser()
        return if (user.status == ResultStatus.SUCCESS){
            ResultWrapper.success(data = user.data?.followers)
        }else{
            ResultWrapper.error(message = user.message, exception = user.exception)
        }
    }

    suspend fun getUserFollows(): ResultWrapper<List<Follower>> {
        // Return List of follows
        val user = repository.getUser()
        return if (user.status == ResultStatus.SUCCESS){
            ResultWrapper.success(data = user.data?.follows)
        }else{
            ResultWrapper.error(message = user.message, exception = user.exception)
        }
    }

}