package minesweeper

val rows = 9
val cols = 9
val grid = Array(rows * cols) {'.'}
val actualGrid = Array(rows * cols) {'.'}
var win = false
var lose = false

fun main() {
    setUpGrid()
    while (!win && !lose) {
        println("Set/delete mines marks (x and y coordinates):")
        checkInput(readln())
    }

    if(win) println("Congratulations! You found all the mines!")
    if(lose) println("You stepped on a mine and failed!")
}

fun setUpGrid() {
    println("How many mines do you want on the field?")
    repeat(readln().toInt()) {
        actualGrid[it] = 'X'
    }
    actualGrid.shuffle()

    actualGrid.toList().chunked(cols).forEachIndexed { row, chars ->
        if (!chars.contains('X')) return@forEachIndexed
        chars.forEachIndexed { col, char ->
            if (char == 'X') {
                val pos = row * cols + col
                if (row != 0) {
                    checkAndReplaceCount(pos - rows)
                    if (col != 0) checkAndReplaceCount(pos - rows - 1)
                    if (col != cols - 1) checkAndReplaceCount(pos - rows + 1)
                }
                if (row != rows - 1) {
                    checkAndReplaceCount(pos + rows)
                    if (col != 0) checkAndReplaceCount(pos + rows - 1)
                    if (col != cols - 1) checkAndReplaceCount(pos + rows + 1)
                }
                if (col != 0) checkAndReplaceCount(pos - 1)
                if (col != cols - 1) checkAndReplaceCount(pos + 1)
            }
        }
    }
    printGrid()
}

fun checkAndReplaceCount(ind : Int) {
    when (actualGrid[ind]) {
        '.' -> actualGrid[ind] = '1'
        in '1'..'8' -> actualGrid[ind] = actualGrid[ind] + 1
    }
}

fun printGrid() {
    println(" |123456789|\n-|---------|")
    if (lose) {
        grid.forEachIndexed { index, c ->
            if (actualGrid[index] == 'X') grid[index] = 'X'
        }
    }
    grid.toMutableList().chunked(cols).forEachIndexed { index, chars ->
        println("${index + 1}|${chars.joinToString("")}|")
    }
    println("-|---------|")
}

fun checkInput(input: String) {
    val col = input.split(" ")[0].toInt() - 1
    val row = input.split(" ")[1].toInt() - 1
    val action = input.split(" ")[2]
    val pos  = row * cols + col
    when {
        grid[pos].isDigit() -> println("There is a number here!")
        grid[pos] == '.' && action == "mine" -> grid[pos] = '*'
        grid[pos] == '*' && action == "mine" -> grid[pos] = '.'
        actualGrid[pos] == 'X' && action == "free" -> lose = true
        grid[pos] == '.' && action == "free" -> {
            checkEmpty(col, row)
        }
    }
    printGrid()

    win = checkWin()
}

fun checkWin() : Boolean {
    val g = grid.map { if (it == '*') 'X' else  '.' }.toTypedArray()
    val ag = actualGrid.map { if (it.isDigit()) '.' else it}.toTypedArray()
    return g.contentEquals(ag)
}

fun checkEmpty(col : Int, row : Int) {
    val pos  = row * cols + col
    if (grid[pos] == '/' || grid[pos].isDigit()) return

    if (actualGrid[pos] == '.') {
        grid[pos] = '/'
        if (row != 0) checkEmpty(col, row - 1)
        if (col < cols - 1) checkEmpty(col + 1, row)
        if (row < rows - 1) checkEmpty(col, row + 1)
        if (col != 0) checkEmpty(col - 1, row)
        if (row != 0 && col != 0) checkEmpty(col - 1, row - 1)
        if (row != 0 && col < cols - 1) checkEmpty(col + 1, row - 1)
        if (row < rows - 1 && col < cols - 1) checkEmpty(col + 1, row + 1)
        if (row < rows - 1 && col != 0) checkEmpty(col - 1, row + 1)
    } else if (actualGrid[pos].isDigit()){
        grid[pos] =  actualGrid[pos]
    }
}
