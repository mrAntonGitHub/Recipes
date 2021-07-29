package com.worldwidemobiledevelopment.recipes.di.modules

import com.worldwidemobiledevelopment.recipes.repository.IRepository
import com.worldwidemobiledevelopment.recipes.repository.Repository
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseAuthorization
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.IFirebaseAuthorization
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule(){

    @Provides
    fun provideRepository(repository: Repository) : IRepository {
        return repository
    }

}