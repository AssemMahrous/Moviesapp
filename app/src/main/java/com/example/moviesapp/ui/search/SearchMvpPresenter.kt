package com.example.moviesapp.ui.search

import androidx.appcompat.widget.SearchView
import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.ui.base.MvpPresenter

interface SearchMvpPresenter<V : SearchMvpView> : MvpPresenter<V> {

    fun getResultsQuery(searchView: SearchView)

    fun incrementPagination()

    fun initializePagination()

    fun stopPagination()

    fun getDataPaginated(query: String, page: Int)

    fun itemClick(it: Result?)

    fun destroy()
}