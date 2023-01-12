package cinema

lateinit var seats : ArrayList<ArrayList<String>>
var rows = 0
var columns = 0
var curIncome = 0
var pTickets = 0

fun main() {
    println("Enter the number of rows:")
    rows = readln().toInt()
    println("Enter the number of seats in each row:")
    columns = readln().toInt()
    seats = setSeats(rows, columns)

    printMenu()
}

fun printMenu() {
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")

    when(readln()) {
        "1" -> printSeats()
        "2" -> buySeat()
        "3" -> printStatistics()
        "0" -> return
    }
}

fun printStatistics() {
    val percent = 100f * pTickets / (rows * columns)
    val totalP = getTotalPrice()

    println("Number of purchased tickets: $pTickets")
    println("Percentage: ${"%.2f".format(percent)}%")
    println("Current income: \$$curIncome")
    println("Total income: \$$totalP")

    printMenu()
}

fun buySeat(){
    var row = 0
    var column = 0
    var isValidSeat = false 

    while (!isValidSeat) {
        println("Enter a row number:")
        row = readln().toInt()
        println("Enter a seat number in that row:")
        column = readln().toInt()

        if (row !in seats.indices || column !in seats[row].indices) {
            println("Wrong input!")
        } else if (seats[row][column] == "B") {
            println("That ticket has already been purchased!")
        } else isValidSeat = true
    }
    
    val price = if (rows * columns <= 60) 10
    else if (row <= rows/2) 10
    else 8

    println("Ticket price: \$$price")
    curIncome += price
    pTickets++
    seats[row][column] = "B"
    printMenu()
}

fun getTotalPrice() : Int {
    return if (rows * columns <= 60) 10 * rows * columns
    else {
        val half = rows/2.toInt()
        val frontP = 10 * (half) * columns
        val backP = 8 * (rows - half) * columns
        frontP + backP
    }
}

fun printSeats() {
    println("Cinema:")
    seats.forEach {
        it.forEach { s ->
            print("$s ")
        }
        println()
    }
    printMenu()
}

fun setSeats(rows : Int, columns : Int) : ArrayList<ArrayList<String>> {
    val seats = ArrayList<ArrayList<String>>()
    for (i in 0..rows) {
        val r = ArrayList<String>()
        for (j in 0..columns) {
            when {
                i == 0 && j == 0 -> r.add(" ")
                i == 0 -> r.add(j.toString())
                j == 0 -> r.add(i.toString())
                else -> r.add("S")
            }
        }
        seats.add(r)
    }
    return seats
}