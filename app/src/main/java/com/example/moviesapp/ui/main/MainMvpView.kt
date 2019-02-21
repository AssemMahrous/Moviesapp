package com.example.moviesapp.ui.main

import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.ui.base.MvpView

interface MainMvpView : MvpView {

    fun initRecyclerView()

    fun setData(results: List<Result?>)

    fun getData()

    fun endPagination()

    fun incrementPagination()

    fun goToPerson(it: Result?)

    fun goToSearch()

}