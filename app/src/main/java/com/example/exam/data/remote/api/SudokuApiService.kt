package com.example.exam.data.remote.api

import com.example.exam.data.remote.dto.SudokuResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SudokuApiService {
    @GET("sudokugenerate")
    suspend fun generatePuzzle(
        @Query("difficulty") difficulty: String
    ): Response<SudokuResponse>
}