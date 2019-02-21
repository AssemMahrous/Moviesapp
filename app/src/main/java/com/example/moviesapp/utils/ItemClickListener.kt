package com.example.moviesapp.utils

import com.example.moviesapp.data.network.models.Profile

interface ItemClickListener {

    fun OnItemClick(profile: Profile)
}