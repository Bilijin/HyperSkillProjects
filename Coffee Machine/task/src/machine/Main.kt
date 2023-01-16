package machine

fun main() {
    val coffeeMachine = CoffeeMachine()
    var exit = false
    while (!exit) {
        if (coffeeMachine.readInput(readln()) != "") exit = true
    }
}

enum class CoffeeMachineState {
    IDLE,
    REFILL_WATER,
    REFILL_MILK,
    REFILL_BEANS,
    REFILL_CUPS,
    SELECT_COFFEE,
    UNKNOWN;

    fun update(n: String): CoffeeMachineState {
        for (en in CoffeeMachineState.values()) {
            if (n.uppercase() == en.name) return en
        }
        return UNKNOWN
    }
}

class CoffeeMachine {
    private var water = 400
    private var milk = 540
    private var beans = 120
    private var disposableCups = 9
    private var money = 550
    private var state = CoffeeMachineState.IDLE

    init {
        reset()
    }

    fun readInput(input: String): String {
        when (state) {
            CoffeeMachineState.IDLE -> {
                if (selectAction(input) != "") return "exit"
            }

            CoffeeMachineState.SELECT_COFFEE -> {
                buyCoffee(input)
            }

            CoffeeMachineState.REFILL_WATER, CoffeeMachineState.REFILL_BEANS, CoffeeMachineState.REFILL_MILK, CoffeeMachineState.REFILL_CUPS -> {
                refillIngredients(input.toInt())
            }

            else -> return ""
        }
        return ""
    }

    private fun selectAction(action: String): String {
        when (action) {
            "buy" -> {
                println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
                updateState("select_coffee")
            }

            "fill" -> {
                refillIngredients(null)
            }

            "take" -> {
                println("I gave you \$$money")
                money = 0
                reset()
            }

            "remaining" -> {
                printLevels()
                reset()
            }
            "exit" -> return "exit"

            else -> {
                println("Unknown Command!!")
                reset()
            }

        }
        return ""
    }

    private fun buyCoffee(coffee: String) {
        val made = when (coffee) {
            "1" -> {
                makeCoffee(w = 250, b = 16, c = 4)
            }

            "2" -> {
                makeCoffee(350, 75, 20, c = 7)
            }

            "3" -> {
                makeCoffee(200, 100, 12, c = 6)
            }

            else -> {
                updateState("idle")
                return
            }
        }
        if (made.isNotEmpty()) println("Sorry, not enough $made!")
        else println("I have enough resources, making you a coffee!")
        reset()
    }

    private fun refillIngredients(quantity: Int?) {
        if (quantity == null) {
            updateState("refill_water")
            println("Write how many ml of water you want to add:")
        } else {
            when (state) {
                CoffeeMachineState.REFILL_WATER -> {
                    water += quantity
                    println("Write how many ml of milk you want to add:")
                    updateState("refill_milk")
                }
                CoffeeMachineState.REFILL_MILK -> {
                    milk += quantity
                    println("Write how many grams of coffee beans you want to add:")
                    updateState("refill_beans")
                }
                CoffeeMachineState.REFILL_BEANS -> {
                    beans += quantity
                    println("Write how many disposable cups you want to add:")
                    updateState("refill_cups")
                }
                CoffeeMachineState.REFILL_CUPS -> {
                    disposableCups += quantity
                    reset()
                }
                else -> reset()
            }
        }
    }

    private fun makeCoffee(w: Int, m: Int? = null, b: Int, c: Int): String {
        if (water < w) return "water"
        if (beans < b) return "coffee beans"
        if (disposableCups < 1) return "disposable cups"

        m?.let {
            milk = if (milk >= m) milk - m else return "milk"
        }
        water -= w
        beans -= b
        disposableCups--
        money += c
        return ""
    }

    private fun printLevels() {
        println()
        println("The coffee machine has:")
        println("$water ml of water")
        println("$milk ml of milk")
        println("$beans g of coffee beans")
        println("$disposableCups disposable cups")
        println("\$$money of money")
        println()
    }

    private fun updateState(strState: String) {
        state = state.update(strState)
    }

    private fun reset() {
        updateState("idle")
        println("Write action (buy, fill, take, remaining, exit):")
    }
}
