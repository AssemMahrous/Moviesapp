package com.example.moviesapp.data.network.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
    @SerializedName("popularity")
    @Expose var popularity: Double, @SerializedName("id") @Expose  var id: Int = 0,
    @SerializedName("profile_path") @Expose var profilePath: String? = null,
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("adult") @Expose var adult: Boolean = false
) : Parcelable