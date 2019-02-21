package com.example.moviesapp.ui.main

import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.ui.base.MvpPresenter

interface MainMvpPresenter<V : MainMvpView> : MvpPresenter<V> {
    fun getData(position: Int)

    fun setData(results: List<Result?>)

    fun incrementPagination()

    fun stopPagination()

    fun itemClick(it: Result?)

    fun searchClick()
}