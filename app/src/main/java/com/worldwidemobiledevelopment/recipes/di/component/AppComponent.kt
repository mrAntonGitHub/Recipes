package com.worldwidemobiledevelopment.recipes.di.component

import com.worldwidemobiledevelopment.recipes.data.SmallCardItem
import com.worldwidemobiledevelopment.recipes.di.modules.AppModule
import com.worldwidemobiledevelopment.recipes.di.modules.FirebaseAuthorizationModule
import com.worldwidemobiledevelopment.recipes.di.modules.FirebaseRepositoryModule
import com.worldwidemobiledevelopment.recipes.di.modules.RepositoryModule
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseAuthorization
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseRepositoryImpl
import com.worldwidemobiledevelopment.recipes.repository.Repository
import com.worldwidemobiledevelopment.recipes.utils.Utils
import com.worldwidemobiledevelopment.recipes.view.home.HomeViewModel
import com.worldwidemobiledevelopment.recipes.view.profile.*
import com.worldwidemobiledevelopment.recipes.view.profile.newRecipe.NewRecipeViewModel
import com.worldwidemobiledevelopment.recipes.view.registration.RegistrationActivity
import com.worldwidemobiledevelopment.recipes.view.registration.RegistrationViewModel
import com.worldwidemobiledevelopment.recipes.view.splash.SplashActivity

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, FirebaseAuthorizationModule::class, FirebaseRepositoryModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(smallCardItem: SmallCardItem)

    // Activity
    fun inject(splashActivity: SplashActivity)
    fun inject(registrationActivity: RegistrationActivity)

    // todo ONLY FOR DEBUG
    fun inject(profileFragment: ProfileFragment)

    // ViewModel
    fun inject(registrationViewModel: RegistrationViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(profileViewModel: ProfileViewModel)

    // Repository
    fun inject(repository: Repository)
    fun inject(firebaseRepositoryImpl: FirebaseRepositoryImpl)
    fun inject(firebaseAuthorization: FirebaseAuthorization)

    fun inject(followersViewModel: FollowAndFollowersViewModel)

    fun inject(myRecipesFragment: MyRecipesFragment)
    fun inject(bookedRecipesFragment: BookedRecipesFragment)


    fun inject(newRecipeViewModel: NewRecipeViewModel)

    fun inject(utils: Utils)
}