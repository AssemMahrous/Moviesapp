package com.example.moviesapp.ui.search

import androidx.appcompat.widget.SearchView
import com.example.moviesapp.data.AppDataManager
import com.example.moviesapp.data.network.models.PopularResponse
import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.ui.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class SearchPresenter<V : SearchMvpView>(dataManager: AppDataManager) : BasePresenter<V>(dataManager),
    SearchMvpPresenter<V> {
    override fun destroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    var compositeDisposable = CompositeDisposable()
    override fun getResultsQuery(searchView: SearchView) {
        val disposable: Disposable = getObservableQuery(searchView)
            .filter { s -> s != "" }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap { t: String -> dataManager.getApi().searchPeople(t, 1) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getObserver())

        compositeDisposable.add(disposable)
    }

    override fun incrementPagination() {
        getMvpView()?.incrementPagination()
    }

    override fun initializePagination() {
        getMvpView()?.initializePagination()
    }

    override fun stopPagination() {
        getMvpView()?.stopPagination()
    }

    override fun getDataPaginated(query: String, page: Int) {
        val disposable: Disposable = dataManager
            .getApi()
            .searchPeople(query, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getObserverPaginate())
        compositeDisposable.add(disposable)
    }

    override fun itemClick(it: Result?) {
        getMvpView()?.goToPerson(it)
    }


    private fun getObservableQuery(searchView: SearchView): Observable<String> {

        val publishSubject = PublishSubject.create<String>()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                publishSubject.onNext(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                publishSubject.onNext(newText)
                return true
            }
        })

        return publishSubject
    }

    private fun getObserver(): DisposableObserver<PopularResponse> {
        return object : DisposableObserver<PopularResponse>() {

            override fun onNext(@NonNull popularResponse: PopularResponse) {
                getMvpView()?.displayResult(popularResponse.results)
                initializePagination()
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
                getMvpView()?.displayError("Error fetching Movie Data")
            }

            override fun onComplete() {}
        }
    }


    private fun getObserverPaginate(): DisposableObserver<PopularResponse> {
        return object : DisposableObserver<PopularResponse>() {

            override fun onNext(@NonNull popularResponse: PopularResponse) {
                getMvpView()?.concatResult(popularResponse.results)
                incrementPagination()
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
                stopPagination()
                getMvpView()?.displayError("Error fetching Movie Data")
            }

            override fun onComplete() {}
        }
    }
}