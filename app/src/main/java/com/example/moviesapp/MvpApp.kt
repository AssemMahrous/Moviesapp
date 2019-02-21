package com.example.moviesapp

import android.app.Application
import com.example.moviesapp.data.AppDataManager
import com.example.moviesapp.data.network.ApiModule
import com.facebook.drawee.backends.pipeline.Fresco

class MvpApp : Application() {
    internal lateinit var dataManager: AppDataManager

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        dataManager = AppDataManager(ApiModule().provideApiService())
    }

    fun getDataManager() : AppDataManager {return dataManager}
}