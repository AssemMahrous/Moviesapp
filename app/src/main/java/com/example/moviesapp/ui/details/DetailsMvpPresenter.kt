package com.example.moviesapp.ui.details

import com.example.moviesapp.data.network.models.Person
import com.example.moviesapp.data.network.models.Profile
import com.example.moviesapp.ui.base.MvpPresenter

interface DetailsMvpPresenter<V : DetailsMvpView> : MvpPresenter<V> {

    fun getData(personId: Int)

    fun personImages(personId: Int)

    fun setData(person: Person?)

    fun setImages(profiles: List<Profile>?)
}