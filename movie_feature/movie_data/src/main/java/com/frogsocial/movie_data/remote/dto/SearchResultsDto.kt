package com.frogsocial.movie_data.remote.dto

import com.google.gson.annotations.SerializedName

class SearchResultsDto<T : Any?> {
    @SerializedName("Search")
    var Search: T? = null

    /* @SerializedName("page")
     val page: Int? = null*/

    @SerializedName("Response")
    var Response: String? = null

    @SerializedName("totalResults")
    var totalResults: String? = null
}