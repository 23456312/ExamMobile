package com.example.exam.domain.usecase

import com.example.exam.domain.repository.SudokuRepository
import javax.inject.Inject

class ClearSavedPuzzleUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke() {
        repository.clearSavedPuzzle()
    }
}