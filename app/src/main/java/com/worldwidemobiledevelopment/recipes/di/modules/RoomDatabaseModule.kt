package com.worldwidemobiledevelopment.recipes.di.modules

//import android.content.Context
//import androidx.room.Room
//import com.worldwidemobiledevelopment.recipes.data.room.RoomDao
//import com.worldwidemobiledevelopment.recipes.data.room.RoomDatabase
//import dagger.Module
//import dagger.Provides
//import javax.inject.Singleton
//
//@Module
//class RoomDatabaseModule(private val application: Context) {
//
//    @Singleton
//    @Provides
//    fun provideDatabase(): RoomDatabase {
//        return Room.databaseBuilder(application, RoomDatabase::class.java, "appDatabase.db")
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideDao(roomDatabase: RoomDatabase): RoomDao {
//        return roomDatabase.roomDao()
//    }
//
//}