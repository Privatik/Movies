package com.io.movies.model

import com.google.gson.annotations.SerializedName

data class ResultCredit(
    @SerializedName("id")
    val id: String,
    @SerializedName("cast")
    val cast: List<Credit>
)

data class Credit (
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profile:String,
    @SerializedName("character")
    val character: String
)