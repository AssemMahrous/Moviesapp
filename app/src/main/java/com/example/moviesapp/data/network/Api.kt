package com.example.moviesapp.data.network

import com.example.moviesapp.data.network.models.Person
import com.example.moviesapp.data.network.models.PopularResponse
import com.example.moviesapp.data.network.models.ProfileImage
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("person/popular")
    fun fetchPopularActors(@Query("page") page: Int): Call<PopularResponse>

    @GET("person/{person_id}")
    fun getPersonDetails(@Path("person_id") personId: Int): Call<Person>

    @GET("person/{person_id}/images")
    fun getPersonImages(@Path("person_id") personId: Int): Call<ProfileImage>

    @GET("search/person")
    fun searchPeople(@Query("query") query: String, @Query("page") page: Int): Observable<PopularResponse>
}