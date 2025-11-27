package com.example.exam.domain.usecase

import com.example.exam.domain.model.SudokuPuzzle
import javax.inject.Inject

class ValidateSolutionUseCase @Inject constructor() {
    operator fun invoke(puzzle: SudokuPuzzle): Boolean {
        if (puzzle.currentState.any { row -> row.any { it == 0 } }) {
            return false
        }

        return puzzle.currentState == puzzle.solution
    }
}
