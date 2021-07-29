package com.worldwidemobiledevelopment.recipes.di.modules

import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseAuthorization
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.IFirebaseAuthorization
import dagger.Module
import dagger.Provides

@Module
class FirebaseAuthorizationModule(){

    @Provides
    fun provideFirebaseAuthorization(firebaseAuthorization: FirebaseAuthorization) : IFirebaseAuthorization {
        return firebaseAuthorization
    }

}