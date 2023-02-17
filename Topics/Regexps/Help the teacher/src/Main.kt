fun main() {
    val report = readLine()!!
    val winRegex = Regex("^[1-9] wrong answers?")
    val winRegex1 = Regex("1 wrong answer")
    val winRegex2 = Regex("[1-9] wrong answer")
    val winRegex3 = Regex("[1-9] wrong answers?")

    println(winRegex.matches(report))
    println(winRegex1.matches(report))
    println(winRegex2.matches(report))
    println(winRegex3.matches(report))
}
