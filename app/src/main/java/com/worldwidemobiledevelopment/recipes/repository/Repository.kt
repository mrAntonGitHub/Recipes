package com.worldwidemobiledevelopment.recipes.repository

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(){

    fun getFoo() : Flow<List<String>>{
        return flow {
            val list = getList()
            emit(list)
        }.flowOn(IO)
    }

    suspend fun getList() : List<String>{
        delay(5_000)
        return listOf("1", "2", "3", "4", "5")
    }

}