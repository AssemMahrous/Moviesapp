package com.example.moviesapp.data.network.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PopularResponse(
    @SerializedName("page")
    @Expose var page: Int, @SerializedName("totalResults") @Expose private var totalResults: Int,
    @SerializedName("total_pages") @Expose var total_pages: Int,
    @SerializedName("results") @Expose var results: List<Result?>
) : Parcelable