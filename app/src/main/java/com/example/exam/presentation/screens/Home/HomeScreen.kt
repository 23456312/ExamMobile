package com.example.exam.presentation.screens.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exam.domain.model.Difficulty
import com.example.exam.domain.model.SudokuSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    hasSavedGame: Boolean,
    onGeneratePuzzle: (SudokuSize, Difficulty) -> Unit,
    onLoadSavedGame: () -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }
    var expandedDifficulty by remember { mutableStateOf(false) }

    // ✨ Y2K gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF000000),
                        Color(0xFF2B0039),
                        Color(0xFFff3eb5)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "★ Y2K Sudoku ★",
                            color = Color(0xFFFF78C4),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1A1A1A).copy(alpha = 0.5f)
                    )
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // ✨ Title
                Text(
                    text = "✨ Welcome, Cutie ✨",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFB5F5),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Let's solve some cute little puzzles 💘",
                    color = Color(0xFFFFE6FA),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 💖 Board size selector
                Text(
                    "Board Size",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                var selectedSize by remember { mutableStateOf(SudokuSize.LARGE) } // default 9x9
                var expandedSize by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expandedSize,
                    onExpandedChange = { expandedSize = !expandedSize }
                ) {
                    OutlinedTextField(
                        value = when (selectedSize) {
                            SudokuSize.SMALL -> "4x4"
                            SudokuSize.LARGE -> "9x9"
                            SudokuSize.MEDIUM -> TODO()
                        },
                        onValueChange = {},
                        readOnly = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSize) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFF3EB5),
                            unfocusedBorderColor = Color(0xFFFFB5F5),
                            cursorColor = Color(0xFFFF3EB5),
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedSize,
                        onDismissRequest = { expandedSize = false }
                    ) {
                        listOf(SudokuSize.SMALL, SudokuSize.LARGE).forEach { size ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        when (size) {
                                            SudokuSize.SMALL -> "4x4"
                                            SudokuSize.LARGE -> "9x9"
                                            SudokuSize.MEDIUM -> TODO()
                                        },
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    selectedSize = size
                                    expandedSize = false
                                }
                            )
                        }
                    }
                }


                Text(
                    "Difficulty",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedDifficulty,
                    onExpandedChange = { expandedDifficulty = !expandedDifficulty }
                ) {
                    OutlinedTextField(
                        value = when (selectedDifficulty) {
                            Difficulty.EASY -> "Easy 💗"
                            Difficulty.MEDIUM -> "Medium 💜"
                            Difficulty.HARD -> "Hard 🖤"
                        },
                        onValueChange = {},
                        readOnly = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDifficulty) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFF3EB5),
                            unfocusedBorderColor = Color(0xFFFFB5F5),
                            cursorColor = Color(0xFFFF3EB5),
                            focusedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDifficulty,
                        onDismissRequest = { expandedDifficulty = false },
                    ) {
                        Difficulty.values().forEach { difficulty ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        when (difficulty) {
                                            Difficulty.EASY -> "Easy 💗"
                                            Difficulty.MEDIUM -> "Medium 💜"
                                            Difficulty.HARD -> "Hard 🖤"
                                        },
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    selectedDifficulty = difficulty
                                    expandedDifficulty = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 🌈 Main buttons
                Y2KButton("🌈 New Game", Color(0xFFFF3EB5)) {
                    onGeneratePuzzle(selectedSize, selectedDifficulty)

                }

                if (hasSavedGame) {
                    Spacer(Modifier.height(16.dp))
                    Y2KButton("💾 Continue Saved Game", Color(0xFF4DDCFF)) {
                        onLoadSavedGame()
                    }
                }
            }
        }
    }
}

@Composable
fun Y2KButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
