package com.widyatama.moviedirectory.core.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {

    @SerializedName("vote_count")
    @Expose
    val voteCount: Int? = null
    @SerializedName("id")
    @Expose
    val id: Int? = null
    @SerializedName("video")
    @Expose
    val video: Boolean? = null
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double? = null
    @SerializedName("title")
    @Expose
    val title: String? = null
    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String? = null
    @SerializedName("overview")
    @Expose
    val overview: String? = null

}
