package com.example.exam.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SudokuResponse(
    @SerializedName("puzzle")
    val puzzle: List<List<Int?>>,

    @SerializedName("solution")
    val solution: List<List<Int>>,

    @SerializedName("difficulty")
    val difficulty: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("grid")
    val grid: List<List<Int>>
)