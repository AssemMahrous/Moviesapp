package com.example.moviesapp.ui.main

import androidx.test.espresso.idling.CountingIdlingResource
import com.example.moviesapp.data.AppDataManager
import com.example.moviesapp.data.network.models.PopularResponse
import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.ui.base.BasePresenter
import com.example.moviesapp.utils.EspressoTestingIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter<V : MainMvpView>(dataManager: AppDataManager) : BasePresenter<V>(dataManager), MainMvpPresenter<V> {
    override fun searchClick() {
        getMvpView()?.goToSearch()
    }

    override fun itemClick(it: Result?) {
        getMvpView()?.goToPerson(it)
    }

    override fun getData(position: Int) {
//        EspressoTestingIdlingResource.increment()
        dataManager
            .getApi()
            .fetchPopularActors(position)
            .enqueue(object : Callback<PopularResponse> {
                override fun onResponse(call: Call<PopularResponse>, response: Response<PopularResponse>) {
                    try {
                        if (response.isSuccessful) {
                            val response1 = response.body()
                            if (position == response1?.total_pages)
                                stopPagination()
                            else
                                incrementPagination()
                            setData(response1!!.results)
//                            EspressoTestingIdlingResource.decrement()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<PopularResponse>, t: Throwable) {
//                    EspressoTestingIdlingResource.decrement()
                }
            })


    }

    override fun setData(results: List<Result?>) {
        getMvpView()!!.setData(results)
    }

    override fun incrementPagination() {
        getMvpView()?.incrementPagination()
    }

    override fun stopPagination() {
        getMvpView()?.endPagination()
    }

}