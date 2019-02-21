package com.example.moviesapp.ui.details

import com.example.moviesapp.data.network.models.Person
import com.example.moviesapp.data.network.models.Profile
import com.example.moviesapp.ui.base.MvpView

interface DetailsMvpView : MvpView {

    fun getData()

    fun setImages(profiles: List<Profile>?)

    fun setData(person: Person?)
}