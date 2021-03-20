package com.worldwidemobiledevelopment.recipes.di.component

import com.worldwidemobiledevelopment.recipes.data.SmallCardItem
import com.worldwidemobiledevelopment.recipes.di.modules.AppModule
import com.worldwidemobiledevelopment.recipes.utils.Utils
import com.worldwidemobiledevelopment.recipes.view.home.HomeFragment
import com.worldwidemobiledevelopment.recipes.view.home.HomeViewModel

import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(homeViewModel: HomeViewModel)
    fun inject(smallCardItem: SmallCardItem)
}