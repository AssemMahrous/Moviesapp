package com.example.moviesapp.data.network.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileImage(
    @SerializedName("profiles") @Expose var profile: List<Profile>? = null
) : Parcelable