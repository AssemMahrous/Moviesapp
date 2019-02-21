package com.example.moviesapp.data.network.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Profile(
    @SerializedName("iso_639_1") @Expose var iso6391: @RawValue Any? = null,
    @SerializedName("width") @Expose var width: Int, @SerializedName("height") @Expose  var height: Int,
    @SerializedName("vote_count") @Expose  var voteCount: Int,
    @SerializedName("vote_average") @Expose  var voteAverage: Double,
    @SerializedName("file_path") @Expose  var filePath: String? = null,
    @SerializedName("aspect_ratio") @Expose  var aspectRatio: Double? = null
) : Parcelable
