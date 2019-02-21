package com.example.moviesapp.ui.base

interface MvpPresenter <V : MvpView> {
    fun onAttach(mvpView: V?)

    fun onDetach()

    fun getMvpView(): V?
}