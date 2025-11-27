package com.example.exam.domain.model

data class SudokuPuzzle(
    val puzzle: List<List<Int>>,
    val solution: List<List<Int>>,
    val currentState: List<List<Int>>,
    val size: SudokuSize,
    val difficulty: Difficulty
)