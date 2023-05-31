package signature

import java.io.File
import kotlin.math.roundToInt

lateinit var medLetters : Map<Char, List<String>>
lateinit var romLetters : Map<Char, List<String>>

fun main() {
    getFont("medium")
    getFont("roman")
    println("Enter name and surname: ")
    val name = getName(readln())

    println("Enter person's status: ")
    val status = getStatus(readln())

    val length = if (name[0].length> status[0].length) name[0].length else status[0].length

    name.forEachIndexed { index, s ->
        name[index] = padString(s, length)
    }

    status.forEachIndexed { index, s ->
        status[index] = padString(s, length)
    }

    println("".padEnd(length + 8, '8'))
    name.forEach { println("88${it}88") }
    status.forEach { println("88${it}88") }
    println("".padEnd(length + 8, '8'))
}

fun getName(inputName : String) : ArrayList<String> {
    val name = Array(romLetters['a']!!.size) {""}.toCollection(ArrayList())
    inputName.split(" ").forEachIndexed { sI, s ->
        for (c in s.indices) {
            romLetters[s[c]]?.let {
                for (i in it.indices) {
                    name[i] += it[i]
                    if (c == s.lastIndex && sI != inputName.split(" ").lastIndex) name[i] += "          "
                }
            }
        }
    }
    return name
}

fun getStatus(stat : String) : ArrayList<String> {
    val status = Array(medLetters['a']!!.size) {""}.toCollection(ArrayList())

    stat.split(" ").forEachIndexed{ sI, s ->
        for (c in s.indices) {
            medLetters[s[c]]?.let {
                for (i in it.indices){
                    status[i] += it[i]
                    if (c == s.lastIndex && sI != stat.split(" ").lastIndex) status[i] += "     "
                }
            }
        }
        if (s.isBlank()) {
            for (i in status.indices) status[i] += "     "
        }
    }
    return status
}

fun getFont(fName : String) {
    val div = File.separator
    val file = File("${div}Users${div}bolaji${div}Downloads${div}${fName}.txt")
    val letters = file.readLines().toMutableList()
    val length = letters[0].split(" ")[0].toInt() + 1
    letters.removeAt(0)

    letters.chunked(length).associateBy (
        { it[0].split(" ")[0].first() }, { it.subList(1, it.size) }
    ).let {
        if (fName == "medium") medLetters = it
        else romLetters = it
    }
}

fun padString(str : String , length : Int) : String{
    var s = str
        val l = s.length
        s = s.padStart((length - l + 4)/2  + l, ' ')
        s = s.padEnd(((length - l + 4)/2.0).roundToInt() + s.length, ' ')
    return s
}