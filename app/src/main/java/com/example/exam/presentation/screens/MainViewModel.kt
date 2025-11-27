package com.example.exam.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam.domain.model.Difficulty
import com.example.exam.domain.model.SudokuPuzzle
import com.example.exam.domain.model.SudokuSize
import com.example.exam.domain.usecase.ClearSavedPuzzleUseCase
import com.example.exam.domain.usecase.GeneratePuzzleUseCase
import com.example.exam.domain.usecase.LoadSavedPuzzleUseCase
import com.example.exam.domain.usecase.SavePuzzleUseCase
import com.example.exam.domain.usecase.ValidateSolutionUseCase
import com.example.exam.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val generatePuzzleUseCase: GeneratePuzzleUseCase,
    private val validateSolutionUseCase: ValidateSolutionUseCase,
    private val savePuzzleUseCase: SavePuzzleUseCase,
    private val loadSavedPuzzleUseCase: LoadSavedPuzzleUseCase,
    private val clearSavedPuzzleUseCase: ClearSavedPuzzleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SudokuUiState>(SudokuUiState.Initial)
    val uiState: StateFlow<SudokuUiState> = _uiState.asStateFlow()

    private val _currentPuzzle = MutableStateFlow<SudokuPuzzle?>(null)
    val currentPuzzle: StateFlow<SudokuPuzzle?> = _currentPuzzle.asStateFlow()

    private val _hasSavedGame = MutableStateFlow(false)
    val hasSavedGame: StateFlow<Boolean> = _hasSavedGame.asStateFlow()

    init {
        checkForSavedGame()
    }

    private fun checkForSavedGame() {
        viewModelScope.launch {
            val savedPuzzle = loadSavedPuzzleUseCase()
            _hasSavedGame.value = savedPuzzle != null
        }
    }

    fun generateNewPuzzle(size: SudokuSize, difficulty: Difficulty) {
        viewModelScope.launch {
            _uiState.value = SudokuUiState.Loading

            when (val result = generatePuzzleUseCase(size, difficulty)) {
                is Resource.Success -> {
                    _currentPuzzle.value = result.data
                    _uiState.value = SudokuUiState.Success("Puzzle generado exitosamente")
                }
                is Resource.Error -> {
                    _uiState.value = SudokuUiState.Error(
                        result.message ?: "Error al generar el puzzle"
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = SudokuUiState.Loading
                }
            }
        }
    }

    fun updateCell(row: Int, col: Int, value: Int) {
        val puzzle = _currentPuzzle.value ?: return

        // No permitir editar celdas fijas
        if (puzzle.puzzle[row][col] != 0) return

        val newState = puzzle.currentState.map { it.toMutableList() }
        newState[row][col] = value

        _currentPuzzle.value = puzzle.copy(currentState = newState)

        // Auto-guardar
        viewModelScope.launch {
            _currentPuzzle.value?.let { savePuzzleUseCase(it) }
        }
    }

    fun validateSolution() {
        val puzzle = _currentPuzzle.value ?: return

        val isValid = validateSolutionUseCase(puzzle)

        _uiState.value = if (isValid) {
            SudokuUiState.ValidationSuccess
        } else {
            SudokuUiState.ValidationError("La solución es incorrecta. ¡Sigue intentando!")
        }
    }

    fun resetPuzzle() {
        val puzzle = _currentPuzzle.value ?: return

        val resetState = puzzle.puzzle.map { it.toList() }
        _currentPuzzle.value = puzzle.copy(currentState = resetState)

        viewModelScope.launch {
            _currentPuzzle.value?.let { savePuzzleUseCase(it) }
        }
    }

    fun savePuzzle() {
        viewModelScope.launch {
            _currentPuzzle.value?.let {
                savePuzzleUseCase(it)
                _hasSavedGame.value = true
                _uiState.value = SudokuUiState.Success("Partida guardada")
            }
        }
    }

    fun loadSavedPuzzle() {
        viewModelScope.launch {
            _uiState.value = SudokuUiState.Loading

            val savedPuzzle = loadSavedPuzzleUseCase()
            if (savedPuzzle != null) {
                _currentPuzzle.value = savedPuzzle
                _uiState.value = SudokuUiState.Success("Partida cargada")
            } else {
                _uiState.value = SudokuUiState.Error("No hay partida guardada")
            }
        }
    }

    fun clearState() {
        _uiState.value = SudokuUiState.Initial
    }

    fun clearCurrentPuzzle() {
        _currentPuzzle.value = null
        viewModelScope.launch {
            clearSavedPuzzleUseCase()
            _hasSavedGame.value = false
        }
    }
}

sealed class SudokuUiState {
    object Initial : SudokuUiState()
    object Loading : SudokuUiState()
    data class Success(val message: String) : SudokuUiState()
    data class Error(val message: String) : SudokuUiState()
    object ValidationSuccess : SudokuUiState()
    data class ValidationError(val message: String) : SudokuUiState()
}