package com.example.exam.presentation.navigation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exam.presentation.screens.Home.HomeScreen
import com.example.exam.presentation.screens.MainViewModel
import com.example.exam.presentation.screens.SudokuUiState
import com.example.exam.presentation.screens.game.GameScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Game : Screen("game")
}

@Composable
fun SudokuNavigation(viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val currentPuzzle by viewModel.currentPuzzle.collectAsState()
    val hasSavedGame by viewModel.hasSavedGame.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Manejar estados de UI
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SudokuUiState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                    viewModel.clearState()
                }
                if (currentPuzzle != null) {
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            }
            is SudokuUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                    viewModel.clearState()
                }
            }
            is SudokuUiState.ValidationSuccess -> {
                // Mostrar diálogo de éxito
            }
            is SudokuUiState.ValidationError -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                    viewModel.clearState()
                }
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    hasSavedGame = hasSavedGame,
                    onGeneratePuzzle = { size, difficulty ->
                        viewModel.generateNewPuzzle(size, difficulty)
                    },
                    onLoadSavedGame = {
                        viewModel.loadSavedPuzzle()
                    }
                )
            }

            composable(Screen.Game.route) {
                currentPuzzle?.let { puzzle ->
                    GameScreen(
                        puzzle = puzzle,
                        onCellClick = { row, col, value ->
                            viewModel.updateCell(row, col, value)
                        },
                        onValidate = {
                            viewModel.validateSolution()
                        },
                        onReset = {
                            viewModel.resetPuzzle()
                        },
                        onNewGame = {
                            viewModel.clearCurrentPuzzle()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        },
                        onBack = {
                            viewModel.savePuzzle()
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        // Loading overlay
        if (uiState is SudokuUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        // Success Dialog
        if (uiState is SudokuUiState.ValidationSuccess) {
            AlertDialog(
                onDismissRequest = { viewModel.clearState() },
                title = { Text("¡Felicidades!") },
                text = { Text("Has completado el Sudoku correctamente. ¡Excelente trabajo!") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.clearState()
                        viewModel.clearCurrentPuzzle()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }) {
                        Text("Nuevo Juego")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.clearState() }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}