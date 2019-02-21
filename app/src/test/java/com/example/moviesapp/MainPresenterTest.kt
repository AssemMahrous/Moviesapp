package com.example.moviesapp

import com.example.moviesapp.data.AppDataManager
import com.example.moviesapp.data.network.ApiModule
import com.example.moviesapp.ui.main.MainMvpView
import com.example.moviesapp.ui.main.MainPresenter
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.io.File


@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    lateinit var mockServer: MockWebServer

    lateinit var dataManager: AppDataManager

    lateinit var presenter: MainPresenter<MainMvpView>

    @Mock
    lateinit var view: MainMvpView

    @Before
    fun setUp() {

        mockServer = MockWebServer()

        // Mock a response with status 200 and sample JSON output
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("json/popular.json"))
        // Enqueue request
        mockServer.enqueue(mockResponse)
        mockServer.start()

        dataManager = AppDataManager(ApiModule().provideApiService())
        presenter = MainPresenter(dataManager)
        presenter.onAttach(view)


    }

    @Test
    fun getPeople() {
        presenter.getData(1)

        verify(view)?.incrementPagination()

        // Get the request that was just made
//        val request = mockServer.takeRequest()
        // Make sure we made the request to the required path
//        assertEquals(getJson("json/popular.json"), request.body)
    }

    @After
    fun tearDown() {
        // We're done with tests, shut it down
        mockServer.shutdown()
    }


    fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}