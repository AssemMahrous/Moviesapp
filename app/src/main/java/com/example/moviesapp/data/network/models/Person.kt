package com.example.moviesapp.data.network.models

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Person(
    @SerializedName("birthday")
    @Expose var birthday: String,
    @SerializedName("known_for_department") @Expose var knownForDepartment: String,
    @SerializedName("deathday") @Expose var deathday: @RawValue Any? = null,
    @SerializedName("id") @Expose var id: Int,
    @SerializedName("name") @Expose var name: String,
    @SerializedName("also_known_as") @Expose var alsoKnownAs: List<String>? = null,
    @SerializedName("gender") @Expose var gender: Int,
    @SerializedName("biography") @Expose var biography: String,
    @SerializedName("popularity") @Expose var popularity: Double,
    @SerializedName("place_of_birth") @Expose var placeOfBirth: String,
    @SerializedName("profile_path") @Expose var profilePath: String,
    @SerializedName("adult") @Expose var adult: Boolean,
    @SerializedName("imdb_id") @Expose var imdbId: String,
    @SerializedName("homepage") @Expose var homepage: @RawValue Any? = null) : Parcelable



