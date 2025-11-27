package com.example.exam.domain.repository

import com.example.exam.domain.model.Difficulty
import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.model.SudokuSize
import com.example.exam.util.Resource

interface SudokuRepository {
    suspend fun generatePuzzle(size: SudokuSize, difficulty: Difficulty): Resource<SudokuPuzzle>
    suspend fun savePuzzle(puzzle: SudokuPuzzle)
    suspend fun loadSavedPuzzle(): SudokuPuzzle?
    suspend fun clearSavedPuzzle()
}
