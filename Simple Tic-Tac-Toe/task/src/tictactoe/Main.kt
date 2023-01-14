package tictactoe

val board = Array(3) {CharArray(3) {'_'}}
var firstPlayer = true
var isWin = false
var isDraw = false
var winner = '\u0000'

fun printBoard() {
    println("---------")
    for (row in board) println("| ${row.joinToString(" ")} |")
    println("---------")
}

fun getMove() {
    var row : Int?
    var column : Int?
    var validPoint = false
    while (!validPoint) {
        val g = readln().replace(" ", "").toCharArray()
        row = g[0].digitToIntOrNull()
        column = g[1].digitToIntOrNull()
        when {
            row == null || column == null -> {
                println("You should enter numbers!")
            }
            row !in 1..3 || column !in 1..3 -> {
                println("Coordinates should be from 1 to 3!")
            }
            board[row - 1][column - 1] != '_' -> {
                println("This cell is occupied! Choose another one!")
            }
            else -> {
                board[row - 1][column - 1] = if (firstPlayer)'X' else 'O'
                validPoint = true
                firstPlayer = !firstPlayer
            }
        }
    }
    printBoard()
    checkBoard()
}

fun checkBoard() {
    for (i in board.indices) {
        if (board[i].contains('_')) continue
        if (board[i].toSet().size == 1 && !board[i].contains('_')) winner = board[i][0]
        else if (board[0][i] == board[1][i] && board[1][i]  == board[2][i]) winner = board[0][i]
    }
    if (winner == '\u0000' && board[1][1] != '_') {
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) winner = board[1][1]
        else if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) winner = board[1][1]
        else if (!board[0].contains('_') && !board[1].contains('_') && !board[2].contains('_')) isDraw = true
    }
    if (winner != '\u0000') isWin = true
}

fun main() {
    printBoard()
    while (!isWin && !isDraw) {
        getMove()
    }

    if (isWin) println("$winner wins")
    else println("Draw")
}