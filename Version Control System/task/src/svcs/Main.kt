package svcs

import java.io.File

fun main(args: Array<String>) {
    val svcs = Svcs(args)

    if (args.isEmpty()) return

    when (val command = args[0]) {
        "config" -> svcs.doConfig()
        "add" -> svcs.addFile()
        "commit" -> {
            if (args.size == 1) println("Message was not passed.")
            else svcs.commitFiles()
        }
        "log" -> svcs.getLog()
        "--help" -> svcs.printHelpPage()
        "checkout" -> svcs.checkOutToCommit()
        else -> println("'$command' is not a SVCS command.")
    }
}

class Svcs(private val args: Array<String>) {

    private val commands = mapOf(
        "config" to "Get and set a username.",
        "add" to "Add a file to the index.",
        "log" to "Show commit logs.",
        "commit" to "Save changes.",
        "checkout" to "Restore a file."
    )
    private val separator: String = File.separator

    private val indexFile = File("vcs${separator}index.txt")
    private val commits = File("vcs${separator}commits")
    private val logFile = File("vcs${separator}log.txt")
    private val configFile = File("vcs${separator}config.txt")

    init {
        createDirs()
        if (!indexFile.exists()) indexFile.createNewFile()
        if (args.isEmpty()) printHelpPage()
    }

    fun addFile() {
        if (args.size > 1) {
            val fileToAdd = File(args[1])
            if (fileToAdd.exists()) {
                if (indexFile.readLines().none { it == args[1] }) indexFile.appendText("${args[1]}\n")
                println("The file '${args[1]}' is tracked.")
            } else println("Can't find '${args[1]}'.")
        } else {
            if (indexFile.readLines().isNotEmpty()) {
                if (indexFile.readLines().isEmpty()) println("Add a file to the index.")
                else {
                    println("Tracked files:")
                    indexFile.forEachLine { println(it) }
                }
            } else println(commands[args[0]])
        }
    }

    fun checkOutToCommit() {
        if (args.size == 1) println("Commit id was not passed.")
        else {
            var commit : File? = null
            commits.listFiles()?.forEach {
                if (it.name == args[1]) commit = it
            }
            if (commit != null) {
                commit!!.listFiles()?.forEach {
                    File(it.name).writeText(it.readText())
                }
                println("Switched to commit ${args[1]}.")
            } else println("Commit does not exist.")
        }
    }

    fun commitFiles() {
        if (commits.listFiles().isNullOrEmpty()) doCommit()
        else {
            val isSameFiles = commits.listFiles()!!.last().listFiles()?.filter {
                it.name !in indexFile.readLines()
            }.isNullOrEmpty()
            val isSame = commits.listFiles()!!.last().listFiles()?.none {
                it.readText() != File(it.name).readText()
            }
            if (isSame == true && isSameFiles) {
                println("Nothing to commit.")
            } else doCommit()
        }
    }

    private fun createDirs() {
        if (!File("vcs").exists()) File("vcs").mkdir()
        if (!File("vcs${separator}commits").exists()) File(("vcs${separator}commits")).mkdir()
    }

    private fun doCommit() {
        val commitId = System.currentTimeMillis()
        val newCommitDir = File("${commits.path}${separator}$commitId")
        newCommitDir.mkdir()
        indexFile.forEachLine {
            File(it).copyTo(File("${newCommitDir.path}${separator}$it"))
        }
        logFile.appendText("commit $commitId\nAuthor: ${configFile.readText()}\n${args[1]}\n\n")
        println("Changes are committed.")
    }

    fun doConfig() {
        if (args.size > 1) {
            configFile.writeText(args[1])
            println("The username is ${configFile.readText()}.")
        } else {
            if (configFile.exists()) {
                println("The username is ${configFile.readText()}.")
            } else {
                println("Please, tell me who you are.")
            }
        }
    }

    fun getLog() {
        if (logFile.exists()) {
            logFile.readText().split("\n\n").reversed().forEach {
                println(it)
            }
        } else println("No commits yet.")
    }

    fun printHelpPage() {
        println("These are SVCS commands:")
        commands.forEach { (k, v) ->
            println("${k.padEnd(11, ' ')}$v")
        }
    }
}
