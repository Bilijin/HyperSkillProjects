package encryptdecrypt

import java.io.File

fun main(args: Array<String>) {
    val action = if (args.contains("-mode")) args[args.indexOf("-mode") + 1] else "enc"
    val key = if (args.contains("-key")) args[args.indexOf("-key") + 1].toInt() else 0
    val alg = if (args.contains("-alg")) args[args.indexOf("-alg") + 1] else "shift"
    val string = if (args.contains("-data")) args[args.indexOf("-data") + 1]
    else if (args.contains("-in")) {
        val file = File(args[args.indexOf("-in") + 1])
        if (file.exists()) file.readText()
        else {
            println("Error! The specified file cannot be found")
            return
        }
    } else ""

    val value = when {
        action == "enc" && alg == "shift" -> string.map {
            if (it.isLetter()) encryptShift(it, key)
            else it
        }.joinToString("")

        action == "enc" && alg == "unicode" -> string.map { encryptUnicode(it, key) }.joinToString("")
        action == "dec" && alg == "shift" -> string.map {
            if (it.isLetter()) decryptShift(it, key)
            else it
        }.joinToString("")

        action == "dec" && alg == "unicode" -> string.map { decryptUnicode(it, key) }.joinToString("")
        else -> ""
    }

    if (args.contains("-out")) File(args[args.indexOf("-out") + 1]).writeText(value)
    else println(value)
}

fun encryptUnicode(char: Char, key: Int) = char + key

fun decryptUnicode(char: Char, key: Int) = char - key

fun encryptShift(char: Char, key: Int) = if (char.isUpperCase()) Char((char + key - 'A') % 26 + 'A'.code)
else Char((char + key - 'a') % 26 + 'a'.code)

fun decryptShift(char: Char, key: Int) =
    if ((char.isUpperCase() && char - key < 'A') || (char.isLowerCase() && char - key < 'a')) {
        char - key + 26
    } else char - key