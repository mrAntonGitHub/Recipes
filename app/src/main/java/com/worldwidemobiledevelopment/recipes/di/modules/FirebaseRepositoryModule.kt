package com.worldwidemobiledevelopment.recipes.di.modules

import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseRepositoryImpl
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseRepository
import dagger.Module
import dagger.Provides

@Module
class FirebaseRepositoryModule(){

    @Provides
    fun provideFirebaseRepository(firebaseRepositoryImpl: FirebaseRepositoryImpl) : FirebaseRepository {
        return firebaseRepositoryImpl
    }

}