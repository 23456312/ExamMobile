package com.example.exam.domain.usecase

import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.repository.SudokuRepository
import javax.inject.Inject

class SavePuzzleUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(puzzle: SudokuPuzzle) {
        repository.savePuzzle(puzzle)
    }
}