package com.worldwidemobiledevelopment.recipes.di.component

import com.worldwidemobiledevelopment.recipes.data.SmallCardItem
import com.worldwidemobiledevelopment.recipes.di.modules.AppModule
import com.worldwidemobiledevelopment.recipes.view.home.HomeViewModel
import com.worldwidemobiledevelopment.recipes.view.registration.RegistrationActivity
import com.worldwidemobiledevelopment.recipes.view.registration.RegistrationViewModel
import com.worldwidemobiledevelopment.recipes.view.splash.SplashActivity

import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(homeViewModel: HomeViewModel)
    fun inject(smallCardItem: SmallCardItem)
    fun inject(registrationViewModel: RegistrationViewModel)
    fun inject(splashActivity: SplashActivity)
    fun inject(registrationActivity: RegistrationActivity)
}