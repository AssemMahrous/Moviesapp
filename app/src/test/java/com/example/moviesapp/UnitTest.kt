package com.example.moviesapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.moviesapp.data.AppDataManager
import com.example.moviesapp.data.network.ApiModule
import com.example.moviesapp.ui.main.MainMvpView
import com.example.moviesapp.ui.main.MainPresenter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import android.R.attr.password
import com.example.moviesapp.data.network.Api
import com.example.moviesapp.data.network.models.PopularResponse
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class UnitTest {


    lateinit var dataManager: AppDataManager

    lateinit var presenter: MainPresenter<MainMvpView>

    @Mock
    lateinit var view: MainMvpView

    @Before
    fun setup() {

        val mockedApiInterface = Mockito.mock<Api>(Api::class.java)
        val mockedCall = Mockito.mock(Call::class.java) as (Call<PopularResponse>)

        Mockito.`when`(mockedApiInterface.fetchPopularActors(1)).thenReturn(mockedCall)

        Mockito.doAnswer(object : Answer<Callback<PopularResponse>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Callback<PopularResponse>? {
                val callback = invocation.getArgument<Callback<PopularResponse>>(0)

                callback.onResponse(mockedCall, Response.success(Mockito.mock(PopularResponse::class.java)))
                // or callback.onResponse(mockedCall, Response.error(404. ...);
                // or callback.onFailure(mockedCall, new IOException());

                return null
            }
        }).`when`<Call<PopularResponse>>(mockedCall).enqueue(any<Callback<PopularResponse>>())


        dataManager = AppDataManager(mockedApiInterface /*ApiModule().provideApiService()*/)
        presenter = MainPresenter(dataManager)
        presenter.onAttach(view)
    }


    @Test
    fun getPeople() {
        presenter.getData(1)
        verify(view)?.incrementPagination();
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter.onDetach()
    }
}