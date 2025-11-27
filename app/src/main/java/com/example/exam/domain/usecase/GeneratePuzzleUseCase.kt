package com.example.exam.domain.usecase

import com.example.exam.domain.model.Difficulty
import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.model.SudokuSize
import com.example.exam.domain.repository.SudokuRepository
import com.example.exam.util.Resource
import javax.inject.Inject

class GeneratePuzzleUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(size: SudokuSize, difficulty: Difficulty): Resource<SudokuPuzzle> {
        return repository.generatePuzzle(size, difficulty)
    }
}
