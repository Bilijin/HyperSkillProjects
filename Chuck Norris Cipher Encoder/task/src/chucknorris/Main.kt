package chucknorris

fun main() {
    var exit = false

    while (!exit) {
        println("Please input operation (encode/decode/exit):")
        when (val command = readln()) {
            "encode" -> convertToChuck()
            "decode" -> convertFromChuck()
            "exit" -> exit = true
            else -> println("There is no '$command' operation")
        }
    }
    println("Bye!")
}

fun convertToChuck() {
    println("Input string:")
    var binary = ""
    for (char in readln()) {
        binary += Integer.toBinaryString(char.code).padStart(7, '0')
    }

    var result = ""
    var pre = binary[0]
    var count = 0
    for (i in binary.indices) {
        if (binary[i] == pre) {
            count++
            if (i != binary.lastIndex) continue
        }
        val d = if (pre == '0') "00" else "0"
        result += " $d ${IntArray(count) { 0 }.joinToString("")}"
        count = 1
        if (i == binary.lastIndex && binary[i] != pre) {
            val f = if (binary[i] == '0') "00" else "0"
            result += " $f ${IntArray(count) { 0 }.joinToString("")}"
        }
        pre = binary[i]
    }
    println("Encoded string:")
    println(result.trim())
}

fun convertFromChuck() {
    println("Input encoded string:")
    val str = readln()
    var bin = ""
    val split = str.split(" ")

    if (str.toSet() != setOf('0', ' ') || split.size % 2 != 0) {
        println("not valid")
        return
    }

    for (i in 0 until split.size - 1 step 2) {
        if (split[i] != "0" && split[i] != "00") {
            println("not valid")
            return
        }
        bin += IntArray(split[i + 1].length) { if (split[i] == "0") 1 else 0 }.joinToString("")
    }
    if (bin.length % 7 != 0) {
        println("not valid")
        return
    }
    println("Decoded string:")
    println(bin.chunked(7).map { num -> Char(Integer.parseInt(num, 2)) }.joinToString(""))
}