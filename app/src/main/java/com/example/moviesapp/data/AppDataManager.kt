package com.example.moviesapp.data

import android.content.Context
import com.example.moviesapp.data.network.Api

class AppDataManager(apiC: Api) {

    private val api: Api = apiC

    fun getApi(): Api {
        return api
    }
}