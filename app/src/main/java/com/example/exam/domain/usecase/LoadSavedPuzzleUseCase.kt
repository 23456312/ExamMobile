package com.example.exam.domain.usecase

import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.repository.SudokuRepository
import javax.inject.Inject

class LoadSavedPuzzleUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(): SudokuPuzzle? {
        return repository.loadSavedPuzzle()
    }
}

