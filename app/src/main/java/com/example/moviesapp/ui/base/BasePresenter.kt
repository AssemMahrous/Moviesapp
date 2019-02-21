package com.example.moviesapp.ui.base

import com.example.moviesapp.data.AppDataManager

open class BasePresenter<V : MvpView>(val dataManager: AppDataManager) : MvpPresenter<V> {

    private var mMvpView: V? = null
    override fun onAttach(mvpView: V?) {
        mMvpView = mvpView
    }

    override fun onDetach() {
        mMvpView = null
    }

    override fun getMvpView(): V? = mMvpView

}