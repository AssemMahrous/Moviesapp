package com.example.moviesapp.ui.search

import com.example.moviesapp.ui.base.MvpView
import com.example.moviesapp.data.network.models.Result

interface SearchMvpView : MvpView {

    fun setViews()

    fun displayResult(results: List<Result?>)

    fun displayError(s: String)

    fun concatResult(results: List<Result?>)

    fun incrementPagination()

    fun stopPagination()

    fun initializePagination()

    fun goToPerson(it: Result?)
}