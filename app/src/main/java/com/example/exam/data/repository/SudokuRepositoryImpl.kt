package com.example.exam.data.repository

import com.example.exam.data.local.SudokuLocalDataSource
import com.example.exam.data.mapper.toDomain
import com.example.exam.data.remote.api.SudokuApiService
import com.example.exam.domain.model.Difficulty
import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.model.SudokuSize
import com.example.exam.domain.repository.SudokuRepository
import com.example.exam.util.Resource
import javax.inject.Inject

class SudokuRepositoryImpl @Inject constructor(
    private val apiService: SudokuApiService,
    private val localDataSource: SudokuLocalDataSource
) : SudokuRepository {

    override suspend fun generatePuzzle(
        size: SudokuSize,
        difficulty: Difficulty
    ): Resource<SudokuPuzzle> {
        return try {
            val response = apiService.generatePuzzle(
                difficulty = difficulty.apiValue
            )

            if (response.isSuccessful && response.body() != null) {
                val sudokuResponse = response.body()!!

                // Convert null → 0 here
                val puzzleGrid = sudokuResponse.puzzle.map { row ->
                    row.map { it ?: 0 }
                }

                val puzzle = SudokuPuzzle(
                    puzzle = puzzleGrid,
                    solution = sudokuResponse.solution ?: emptyList(),
                    currentState = puzzleGrid.map { it.toMutableList() },
                    size = SudokuSize.LARGE,
                    difficulty = difficulty
                )

                Resource.Success(puzzle)

            } else {
                Resource.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error desconocido")
        }
    }


    override suspend fun savePuzzle(puzzle: SudokuPuzzle) {
        localDataSource.savePuzzle(puzzle)
    }

    override suspend fun loadSavedPuzzle(): SudokuPuzzle? {
        return localDataSource.loadPuzzle()
    }

    override suspend fun clearSavedPuzzle() {
        localDataSource.clearPuzzle()
    }
}