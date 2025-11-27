package com.example.exam.domain.model

enum class SudokuSize(
    val width: Int,
    val height: Int,
    val boxWidth: Int,
    val boxHeight: Int
) {
    SMALL(4, 4, 2, 2),
    MEDIUM(6, 6, 2, 3),
    LARGE(9, 9, 3, 3);

    companion object {
        fun fromDimensions(width: Int, height: Int): SudokuSize {
            return values().find { it.width == width && it.height == height }
                ?: LARGE
        }
    }
}
