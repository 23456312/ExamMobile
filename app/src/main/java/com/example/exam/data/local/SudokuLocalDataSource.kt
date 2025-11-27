package com.example.exam.data.local

import android.content.SharedPreferences
import com.example.exam.domain.model.Difficulty
import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.model.SudokuSize
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SudokuLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val KEY_PUZZLE = "saved_puzzle"
        private const val KEY_SOLUTION = "saved_solution"
        private const val KEY_CURRENT_STATE = "saved_current_state"
        private const val KEY_SIZE_WIDTH = "saved_size_width"
        private const val KEY_SIZE_HEIGHT = "saved_size_height"
        private const val KEY_DIFFICULTY = "saved_difficulty"
    }

    fun savePuzzle(puzzle: SudokuPuzzle) {
        sharedPreferences.edit().apply {
            putString(KEY_PUZZLE, gson.toJson(puzzle.puzzle))
            putString(KEY_SOLUTION, gson.toJson(puzzle.solution))
            putString(KEY_CURRENT_STATE, gson.toJson(puzzle.currentState))
            putInt(KEY_SIZE_WIDTH, puzzle.size.width)
            putInt(KEY_SIZE_HEIGHT, puzzle.size.height)
            putString(KEY_DIFFICULTY, puzzle.difficulty.name)
            apply()
        }
    }

    fun loadPuzzle(): SudokuPuzzle? {
        return try {
            val puzzleJson = sharedPreferences.getString(KEY_PUZZLE, null) ?: return null
            val solutionJson = sharedPreferences.getString(KEY_SOLUTION, null) ?: return null
            val currentStateJson = sharedPreferences.getString(KEY_CURRENT_STATE, null) ?: return null
            val width = sharedPreferences.getInt(KEY_SIZE_WIDTH, 0)
            val height = sharedPreferences.getInt(KEY_SIZE_HEIGHT, 0)
            val difficultyName = sharedPreferences.getString(KEY_DIFFICULTY, null) ?: return null

            val listType = object : TypeToken<List<List<Int>>>() {}.type
            val puzzle: List<List<Int>> = gson.fromJson(puzzleJson, listType)
            val solution: List<List<Int>> = gson.fromJson(solutionJson, listType)
            val currentState: List<List<Int>> = gson.fromJson(currentStateJson, listType)

            SudokuPuzzle(
                puzzle = puzzle,
                solution = solution,
                currentState = currentState,
                size = SudokuSize.fromDimensions(width, height),
                difficulty = Difficulty.valueOf(difficultyName)
            )
        } catch (e: Exception) {
            null
        }
    }

    fun clearPuzzle() {
        sharedPreferences.edit().apply {
            remove(KEY_PUZZLE)
            remove(KEY_SOLUTION)
            remove(KEY_CURRENT_STATE)
            remove(KEY_SIZE_WIDTH)
            remove(KEY_SIZE_HEIGHT)
            remove(KEY_DIFFICULTY)
            apply()
        }
    }
}