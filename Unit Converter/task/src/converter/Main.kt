package converter

import kotlin.system.exitProcess

val inputRegex = Regex("-?(\\d*)\\.?(\\d+)( [a-zA-Z]+){1,2} (in|to|convertto)( [a-zA-Z]+){1,2}")

fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        validateInput(readln().lowercase())
    }
}

fun validateInput(input: String) {
    if (input == "exit") exitProcess(0)
    if (!input.matches(inputRegex)) {
        println("Parse error\n")
        return
    }
    val split = input.split(" ").toMutableList()

    val measFrom = if (split[1] == "degree" || split[1] == "degrees") Measurements.getMeasurement("${split[1]} ${split[2]}")
    else Measurements.getMeasurement(split[1])

    val measTo = if (split[split.size - 2] == "degree" || split[split.size - 2] == "degrees") Measurements.getMeasurement("${split[split.size - 2]} ${split[split.size - 1]}")
    else Measurements.getMeasurement(split[split.size - 1])

    if (measFrom == Measurements.UNKNOWN || measTo == Measurements.UNKNOWN || measFrom.type != measTo.type) {
        println("Conversion from ${measFrom.reps[2]} to ${measTo.reps[2]} is impossible\n")
        return
    }
    val num = split[0].toDouble()
    if (num < 0 && (measFrom.type == MeasurementType.LENGTH || measFrom.type == MeasurementType.WEIGHT)) {
        println("${measFrom.type.value} shouldn't be negative\n")
        return
    }

    val result = if (measFrom.type != MeasurementType.TEMPERATURE) num * measFrom.hub / measTo.hub
    else {
        var init = num
        if (measFrom == Measurements.FAHRENHEIT) init = (num - 32) * 5/9
        else if (measFrom == Measurements.KELVINS) init = num - 273.15
        if (measTo == Measurements.FAHRENHEIT) init * 9/5 + 32
        else if (measTo == Measurements.KELVINS) init + 273.15
        else init
    }

    val s = "$num ${if (num == 1.0) measFrom.reps[1] else measFrom.reps[2]} is $result ${if (result == 1.0) measTo.reps[1] else measTo.reps[2]}\n"
    println(s)
}

enum class Measurements(val reps : Array<String>, val type : MeasurementType, val hub : Double) {
    GRAM(arrayOf("g", "gram", "grams"), MeasurementType.WEIGHT, 1.0),
    KILOGRAM(arrayOf("kg", "kilogram", "kilograms"), MeasurementType.WEIGHT, 1000.0),
    MILLIGRAM(arrayOf("mg", "milligram", "milligrams"), MeasurementType.WEIGHT, 0.001),
    POUND(arrayOf("lb", "pound", "pounds"), MeasurementType.WEIGHT, 453.592),
    OUNCE(arrayOf("oz", "ounce", "ounces"), MeasurementType.WEIGHT, 28.3495),
    METER(arrayOf("m", "meter", "meters"), MeasurementType.LENGTH, 1.0),
    KILOMETER(arrayOf("km", "kilometer", "kilometers"), MeasurementType.LENGTH, 1000.0),
    CENTIMETER(arrayOf("cm", "centimeter", "centimeters"), MeasurementType.LENGTH, 0.01),
    MILLIMETER(arrayOf("mm", "millimeter", "millimeters"), MeasurementType.LENGTH, 0.001),
    MILE(arrayOf("mi", "mile", "miles"), MeasurementType.LENGTH, 1609.35),
    YARD(arrayOf("yd", "yard", "yards"), MeasurementType.LENGTH, 0.9144),
    FOOT(arrayOf("ft", "foot", "feet"), MeasurementType.LENGTH, 0.3048),
    INCH(arrayOf("in", "inch", "inches"), MeasurementType.LENGTH, 0.0254),
    CELSIUS(arrayOf("dc", "degree Celsius", "degrees Celsius", "degree celsius", "degrees celsius", "celsius", "c"), MeasurementType.TEMPERATURE, 1.0),
    FAHRENHEIT(arrayOf("df", "degree Fahrenheit", "degrees Fahrenheit", "degree fahrenheit", "degrees fahrenheit", "fahrenheit", "f"), MeasurementType.TEMPERATURE, 32.0 * 5.0/9.0),
    KELVINS(arrayOf("k", "kelvin", "kelvins"), MeasurementType.TEMPERATURE, 273.15),
    UNKNOWN(arrayOf("???", "???", "???"), MeasurementType.UNKNOWN, 0.0);

    companion object {
        fun getMeasurement(rep : String) =  Measurements.values().find { it.reps.contains(rep) } ?: UNKNOWN
    }
}

enum class MeasurementType(val value : String) {
    WEIGHT("Weight"),
    LENGTH("Length"),
    TEMPERATURE("Temperature"),
    UNKNOWN("Unknown")
}