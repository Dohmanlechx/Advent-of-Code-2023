import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val lines = Reader.input(3)
    val symbolCoordinates = mutableListOf<Point>()
    val numbers = lines.numbersAdjacentToSymbols { symbolCoordinates.add(it) }
    val adjacentToSymbols = numbers.filter { it.isAdjacent(symbolCoordinates) }

    d3a(adjacentToSymbols)
    d3b(adjacentToSymbols)
}

fun d3a(adjacentToSymbols: List<Number>) {
    val sum = adjacentToSymbols.map(Number::value).sum()
    require(sum == 498559)
}

fun d3b(adjacentToSymbols: List<Number>) {
    val numbersHavingSameGear =
        adjacentToSymbols.filter { number -> adjacentToSymbols.count { it.gear == number.gear } > 1 }
    val gears = numbersHavingSameGear.groupBy(Number::gear)

    var sum = 0
    for (v in gears.values) {
        val n = v.map { n -> n.value }
        sum += n.reduce { x, y -> x * y }
    }

    require(sum == 72246648)
}

fun List<String>.numbersAdjacentToSymbols(onSymbolFound: (Point) -> Unit): List<Number> {
    val numbers = mutableListOf<Number>()

    forEachIndexed { y, line ->
        var numberStr = ""
        var number = Number()

        fun maybeAddNumber(x: Int) {
            if (numberStr.isNotBlank()) {
                numbers.add(number.copy(value = numberStr.toInt(), lastDigit = Point(x - 1, y)))
                numberStr = ""
                number = Number()
            }
        }

        line.forEachIndexed { x, sign ->
            if (sign.isDigit()) {
                numberStr += sign

                if (number.firstDigit == null) {
                    number = number.copy(firstDigit = Point(x, y))
                }
            } else {
                if (sign != '.') {
                    onSymbolFound(Point(x, y))
                }
                maybeAddNumber(x = x)
            }
        }
        maybeAddNumber(x = line.length - 1)
    }

    return numbers
}

data class Point(var x: Int = 0, var y: Int = 0)

data class Number(
    var value: Int = -1,
    var firstDigit: Point? = null,
    var lastDigit: Point? = null,
    var gear: Point? = null
) {
    fun isAdjacent(symbolCoordinate: List<Point>): Boolean {
        for (symbol in symbolCoordinate) {
            val adjacent =
                (symbol.x - firstDigit!!.x).absoluteValue <= 1 && (symbol.y - firstDigit!!.y).absoluteValue <= 1 ||
                        (symbol.x - lastDigit!!.x).absoluteValue <= 1 && (symbol.y - lastDigit!!.y).absoluteValue <= 1

            if (adjacent) {
                gear = symbol
                return true
            }

        }

        return false
    }
}