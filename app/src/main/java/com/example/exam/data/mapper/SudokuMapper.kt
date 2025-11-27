package com.example.exam.data.mapper

import com.example.exam.data.remote.dto.SudokuResponse
import com.example.exam.domain.model.Difficulty
import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.model.SudokuSize

fun SudokuResponse.toDomain(difficulty: Difficulty): SudokuPuzzle {
    val size = SudokuSize.LARGE

    val puzzleGrid = puzzle.map { row ->
        row.map { it ?: 0 }
    }

    return SudokuPuzzle(
        puzzle = puzzleGrid,
        solution = solution,
        currentState = puzzleGrid.map { it.toList() },
        size = size,
        difficulty = difficulty
    )
}