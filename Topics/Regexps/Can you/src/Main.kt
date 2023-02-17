fun main() {
    val regCanDo = Regex("I can\'?t? do my homework on time!")
    val answer = readln()

    println(regCanDo.matches(answer))
}
