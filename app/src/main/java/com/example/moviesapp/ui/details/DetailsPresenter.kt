package com.example.moviesapp.ui.details

import com.example.moviesapp.data.AppDataManager
import com.example.moviesapp.data.network.models.Person
import com.example.moviesapp.data.network.models.Profile
import com.example.moviesapp.data.network.models.ProfileImage
import com.example.moviesapp.ui.base.BasePresenter
import com.example.moviesapp.utils.EspressoTestingIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsPresenter<V : DetailsMvpView>(dataManager: AppDataManager) : BasePresenter<V>(dataManager),
    DetailsMvpPresenter<V> {
    override fun personImages(personId: Int) {
        EspressoTestingIdlingResource.increment()
        dataManager
            .getApi()
            .getPersonImages(personId)
            .enqueue(object : Callback<ProfileImage> {
                override fun onResponse(call: Call<ProfileImage>, response: Response<ProfileImage>) {
                    try {
                        if (response.isSuccessful())
                            setImages(response.body()!!.profile)
                        EspressoTestingIdlingResource.decrement()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ProfileImage>, t: Throwable) {
                    EspressoTestingIdlingResource.decrement()
                }
            })
    }

    override fun setData(person: Person?) {
        getMvpView()?.setData(person)
    }

    override fun setImages(profiles: List<Profile>?) {
        getMvpView()?.setImages(profiles)
    }

    override fun getData(personId: Int) {
        dataManager
            .getApi()
            .getPersonDetails(personId)
            .enqueue(object : Callback<Person> {
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    try {
                        if (response.isSuccessful)
                            setData(response.body());
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }
                }

                override fun onFailure(call: Call<Person>, t: Throwable) {

                }
            })
    }

}