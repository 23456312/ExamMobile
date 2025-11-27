package com.example.exam.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exam.domain.model.SudokuPuzzle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    puzzle: SudokuPuzzle,
    onCellClick: (row: Int, col: Int, value: Int) -> Unit,
    onValidate: () -> Unit,
    onReset: () -> Unit,
    onNewGame: () -> Unit,
    onBack: () -> Unit
) {
    var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var showNumberPicker by remember { mutableStateOf(false) }

    // ✨ Number Picker (Y2K Pop-up)
    if (showNumberPicker && selectedCell != null) {
        Y2KNumberPicker(
            maxNumber = puzzle.size.width,
            onNumberSelected = { number ->
                selectedCell?.let { (row, col) ->
                    onCellClick(row, col, number)
                }
                showNumberPicker = false
                selectedCell = null
            },
            onDismiss = {
                showNumberPicker = false
                selectedCell = null
            }
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "★ Y2K Sudoku ★",
                        fontSize = 22.sp,
                        color = Color(0xFFFF3EB5),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFF78C4)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        },
        containerColor = Color(0xFF000000)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🟪 Holographic Sudoku board
            SudokuBoardY2K(
                puzzle = puzzle,
                selectedCell = selectedCell,
                onCellClick = { row, col ->
                    if (puzzle.puzzle[row][col] == 0) {
                        selectedCell = row to col
                        showNumberPicker = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            BubbleButton("💖 Validate", Color(0xFFFF3EB5)) { onValidate() }
            Spacer(Modifier.height(12.dp))
            BubbleButton("🖤 Reset", Color(0xFFB026FF)) { onReset() }
            Spacer(Modifier.height(12.dp))
            BubbleButton("🌈 New Game", Color(0xFF4DDCFF)) { onNewGame() }
        }
        // Overlay Number Picker
        if (showNumberPicker && selectedCell != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable {
                        showNumberPicker = false
                        selectedCell = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Y2KNumberPicker(
                    maxNumber = puzzle.size.width,
                    onNumberSelected = { number ->
                        selectedCell?.let { (row, col) ->
                            onCellClick(row, col, number)
                        }
                        showNumberPicker = false
                        selectedCell = null
                    },
                    onDismiss = {
                        showNumberPicker = false
                        selectedCell = null
                    }
                )
            }
        }
    }
}
@Composable
fun SudokuCellY2K(
    value: Int,
    isFixed: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val bg = when {
        isSelected -> Color(0xFFB026FF).copy(alpha = 0.6f)
        isFixed -> Color(0xFF2E2E2E)
        else -> Color(0xFF1A1A1A)
    }

    Box(
        modifier = modifier
            .background(bg)
            .border(
                width = 1.dp,
                color = Color(0xFFFF3EB5)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
                fontSize = 22.sp,
                color = if (isFixed) Color(0xFF4DDCFF) else Color.White
            )
        }
    }
}
@Composable
fun BubbleButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Text(
            text,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SudokuBoardY2K(
    puzzle: SudokuPuzzle,
    selectedCell: Pair<Int, Int>?,
    onCellClick: (Int, Int) -> Unit
) {
    val size = puzzle.size.width
    val boxSize = puzzle.size.boxWidth

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .border(3.dp, Color(0xFFFF3EB5)) // Neon pink border
            .background(Color(0xFF0A0A0A))   // Deep emo black
            .padding(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            for (row in 0 until size) {
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until size) {
                        val value = puzzle.currentState[row][col]
                        val isFixed = puzzle.puzzle[row][col] != 0
                        val isSelected = selectedCell == Pair(row, col)

                        SudokuCellY2K(
                            value = value,
                            isFixed = isFixed,
                            isSelected = isSelected,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(
                                    width = if (row % boxSize == 0) 2.dp else 1.dp,
                                    color = Color(0xFFB026FF)
                                )
                                .border(
                                    width = if (col % boxSize == 0) 2.dp else 1.dp,
                                    color = Color(0xFFFF3EB5)
                                )
                                .clickable(enabled = !isFixed) {
                                    onCellClick(row, col)
                                }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Y2KNumberPicker(
    maxNumber: Int,
    onNumberSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                ":3 Pick a Number ✨",
                color = Color(0xFFFF78C4),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(220.dp)
            ) {
                items(maxNumber) { index ->
                    val number = index + 1
                    Button(
                        onClick = { onNumberSelected(number) },
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3EB5),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            number.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Delete Button
                item {
                    OutlinedButton(
                        onClick = { onNumberSelected(0) },
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFF2B0039),
                            contentColor = Color(0xFFFFB5F5)
                        )
                    ) {
                        Text("Clear", fontSize = 14.sp)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF4DDCFF))
            }
        }
    }
}


