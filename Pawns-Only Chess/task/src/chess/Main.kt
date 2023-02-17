package chess

import kotlin.system.exitProcess

val board = Array(8) { Array(8) { " " } }

var player1 = ""
var player2 = ""

//P1 = W, P2 = B
var p1turn = true
val moveRegex = Regex("^[a-h][1-8][a-h][1-8]$")

var lastMovedBlack = intArrayOf()
var lastMovedWhite = intArrayOf()

fun main() {
    init()
    println("$player1's turn:")
    getMove(readln())
}

fun init() {
    repeat(8) {
        board[1][it] = "B"
        board[6][it] = "W"
    }

    println("Pawns-Only Chess")
    println("First Player's name:")
    player1 = readln()
    println("Second Player's name:")
    player2 = readln()
    printBoard()
}

fun printBoard() {
    val rowDivider = "  +---+---+---+---+---+---+---+---+"
    val colDivider = '|'

    println(rowDivider)

    repeat(8) { row ->
        print("${8 - row} ")
        print(colDivider)
        repeat(8) { col ->
            print(" ${board[row][col]} $colDivider")
        }
        println()
        println(rowDivider)
    }

    println("    a   b   c   d   e   f   g   h  ")
    println()
}

fun getMove(move: String) {
    when {
        moveRegex.matches(move) -> checkMove(move)
        move.equals("exit", true) -> exit()

        else -> println("Invalid Input")
    }
    if (isWin()) {
        println(
            if (p1turn) "Black Wins!"
            else "White Wins!"
        )
        exit()
    } else if (isStaleMate()) {
        println("Stalemate!")
        exit()
    } else {
        println(
            if (p1turn) "$player1's turn:"
            else "$player2's turn:"
        )
        getMove(readln())
    }
}

fun exit() {
    println("Bye!")
    exitProcess(0)
}

fun checkMove(move: String) {
    val moves = move.toCharArray().mapIndexed { index, c ->
        if (index % 2 == 0) c.code - 97
        else 8 - c.digitToInt()
    }.toList()

    if (p1turn) {
        when {
            board[moves[1]][moves[0]] != "W" -> println("No white pawn at ${move[0]}${move[1]}")
            isPassant(moves[3], moves[2], moves[1], moves[0]) -> {
                board[lastMovedBlack[0]][lastMovedBlack[1]] = " "
                movePiece(moves, "W")
            }

            isCapture(moves[3], moves[2], moves[1], moves[0]) && board[moves[3]][moves[2]] == "B" -> movePiece(
                moves,
                "W"
            )

            moves[0] != moves[2] -> println("Invalid Input")
            moves[1] - moves[3] != 1 && (moves[1] - moves[3] != 2 || moves[1] != 6) -> println("Invalid Input")
            board[moves[3]][moves[2]] == "B" -> println("Invalid Input")
            else -> movePiece(moves, "W")
        }
    } else {
        when {
            board[moves[1]][moves[0]] != "B" -> println("No black pawn at ${move[0]}${move[1]}")
            isPassant(moves[3], moves[2], moves[1], moves[0]) -> {
                board[lastMovedBlack[0]][lastMovedBlack[1]] = " "
                movePiece(moves, "B")
            }

            isCapture(moves[3], moves[2], moves[1], moves[0]) && board[moves[3]][moves[2]] == "W" -> movePiece(
                moves,
                "B"
            )

            moves[0] != moves[2] -> println("Invalid Input")
            moves[3] - moves[1] != 1 && (moves[3] - moves[1] != 2 || moves[1] != 1) -> println("Invalid Input")
            board[moves[3]][moves[2]] == "W" -> println("Invalid Input")
            else -> movePiece(moves, "B")
        }
    }
}

fun movePiece(moves: List<Int>, ch: String) {
    board[moves[1]][moves[0]] = " "
    board[moves[3]][moves[2]] = ch
    if (p1turn) lastMovedWhite = intArrayOf(moves[3], moves[2])
    else lastMovedBlack = intArrayOf(moves[3], moves[2])
    printBoard()
    p1turn = !p1turn
}

fun isPassant(x: Int, y: Int, startX: Int, startY: Int): Boolean {
    if (lastMovedBlack.isEmpty() || lastMovedWhite.isEmpty()) return false
    return if (p1turn) {
        isCapture(x, y, startX, startY) && intArrayOf(x + 1, y).contentEquals(lastMovedBlack)

    } else {
        isCapture(x, y, startX, startY) && intArrayOf(x - 1, y).contentEquals(lastMovedWhite)
    }
}

fun isCapture(x: Int, y: Int, startX: Int, startY: Int): Boolean {
    return if (p1turn) {
        x + 1 == startX && (y - 1 == startY || y + 1 == startY)
    } else x - 1 == startX && (y - 1 == startY || y + 1 == startY)
}

fun isWin() = when {
    board[0].contains("W") || board[7].contains("B") -> true
    board.map { it.contains("B") }.toSet().size == 1 -> true
    board.map { it.contains("W") }.toSet().size == 1 -> true
    else -> false
}

fun isStaleMate(): Boolean {
    val isMove = if (p1turn) {
        board.mapIndexed { x, row ->
            row.mapIndexed { y, s ->
                if (s != "W") false
                else {
                    when {
                        board[x - 1][y].isBlank() -> true
                        y != 0 && y != 7 -> board[x - 1][y - 1] == "B" || board[x - 1][y + 1] == "B"
                        y != 0 -> board[x - 1][y - 1] == "B"
                        else -> board[x - 1][y + 1] == "B"
                    }
                }
            }.toSet().contains(true)
        }.toSet().contains(true)
    } else {
        board.mapIndexed { x, row ->
            row.mapIndexed { y, s ->
                if (s != "B") false
                else {
                    when {
                        board[x + 1][y].isBlank() -> true
                        y != 0 && y != 7 -> board[x + 1][y - 1] == "W" || board[x + 1][y + 1] == "W"
                        y != 0 -> board[x + 1][y - 1] == "W"
                        else -> board[x + 1][y + 1] == "W"
                    }
                }
            }.toSet().contains(true)
        }.toSet().contains(true)
    }
    return !isMove
}