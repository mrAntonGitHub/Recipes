package com.worldwidemobiledevelopment.recipes

import android.app.Application
import com.worldwidemobiledevelopment.recipes.di.component.AppComponent
import com.worldwidemobiledevelopment.recipes.di.component.DaggerAppComponent
import com.worldwidemobiledevelopment.recipes.di.modules.AppModule

class Application : Application() {

    companion object{
        lateinit var application: com.worldwidemobiledevelopment.recipes.Application
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        application = this
        initDagger()
    }

    private fun initDagger(){
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }

}