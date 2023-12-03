import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val lines = Reader.input(3)
    val symbolCoordinates = mutableListOf<Point>()
    val numbers = lines.numbersAdjacentToSymbols { symbolCoordinates.add(it) }
    val numbersAdjacentToSymbols = numbers.filter { it.isAdjacent(symbolCoordinates) }

    d3a(numbersAdjacentToSymbols)
    d3b(numbersAdjacentToSymbols)
}

fun d3a(numbersAdjacentToSymbols: List<Number>) {
    val sum = numbersAdjacentToSymbols.sumOf { it.value }
    require(sum == 498559)
}

fun d3b(numbersAdjacentToSymbols: List<Number>) {
    val numbersHavingSameGear = numbersAdjacentToSymbols.filter { number ->
        numbersAdjacentToSymbols.count { it.gear == number.gear } > 1
    }
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

        line.forEachIndexed { x, sign ->
            if (sign.isDigit()) {
                numberStr += sign

                if (number.firstDigit == null) {
                    number = number.copy(firstDigit = Point(x, y))
                }
            } else {
                if (sign != '.') {
                    onSymbolFound.invoke(Point(x, y))
                }

                if (numberStr.isNotBlank()) {
                    numbers.add(
                        number.copy(value = numberStr.toInt(), lastDigit = Point(x - 1, y))
                    )

                    numberStr = ""
                    number = Number()
                }
            }
        }

        if (numberStr.isNotBlank()) {
            numbers.add(
                number.copy(
                    value = numberStr.toInt(),
                    lastDigit = Point(line.length - 1, y)
                )
            )

            numberStr = ""
            number = Number()
        }
    }

    return numbers
}

data class Point(
    var x: Int = 0,
    var y: Int = 0,
)

data class Number(
    var value: Int = -1,
    var firstDigit: Point? = null,
    var lastDigit: Point? = null,
    var gear: Point? = null
) {
    fun isAdjacent(symbolCoordinate: List<Point>): Boolean {
        val f = firstDigit
        val l = lastDigit

        if (f == null || l == null) {
            return false
        }

        for (symbol in symbolCoordinate) {
            val adjacent = (symbol.x - f.x).absoluteValue <= 1 && (symbol.y - f.y).absoluteValue <= 1 ||
                    (symbol.x - l.x).absoluteValue <= 1 && (symbol.y - l.y).absoluteValue <= 1

            if (adjacent) {
                gear = symbol
                return true
            }

        }

        return false
    }
}