import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    d3a()
    d3b()
}

fun d3a() {
    val lines = Reader.input(3)
    val symbolCoordinates = mutableListOf<Coordinate>()
    val numbers = lines.numbersAdjacentToSymbols { symbolCoordinates.add(it) }
    val numbersAdjacentToSymbols = numbers.filter { it.isAdjacent(symbolCoordinates) }
    val sum = numbersAdjacentToSymbols.sumOf { it.value }
    require(sum == 498559)
}

fun d3b() {
    val lines = Reader.input(3)
    val symbolCoordinates = mutableListOf<Coordinate>()
    val numbers = lines.numbersAdjacentToSymbols { symbolCoordinates.add(it) }
    val numbersAdjacentToSymbols = numbers.filter { it.isAdjacent(symbolCoordinates) }
    val numbersHavingSameGear = numbersAdjacentToSymbols.filter { number ->
        numbersAdjacentToSymbols.count { it.gear == number.gear } > 1
    }
    val setByGear = numbersHavingSameGear.groupBy(Number::gear)
    var sum = 0
    setByGear.forEach {
        val n = it.value.map { n -> n.value }
        sum += n.reduce { x, y -> x * y }
    }
    require(sum == 72246648)
}

fun List<String>.numbersAdjacentToSymbols(onSymbolFound: (Coordinate) -> Unit): List<Number> {
    val numbers = mutableListOf<Number>()

    forEachIndexed { y, line ->
        var numberStr = ""
        var number = Number()

        line.forEachIndexed { x, sign ->
            if (sign.isDigit()) {
                numberStr += sign

                if (number.firstDigit == null) {
                    number = number.copy(firstDigit = Coordinate(x, y))
                }
            } else {
                if (sign != '.') {
                    onSymbolFound.invoke(Coordinate(x, y))
                }

                if (numberStr.isNotBlank()) {
                    numbers.add(
                        number.copy(value = numberStr.toInt(), lastDigit = Coordinate(x - 1, y))
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
                    lastDigit = Coordinate(line.length - 1, y)
                )
            )

            numberStr = ""
            number = Number()
        }
    }

    return numbers
}

data class Coordinate(
    var x: Int = 0,
    var y: Int = 0,
)

data class Number(
    var value: Int = -1,
    var firstDigit: Coordinate? = null,
    var lastDigit: Coordinate? = null,
    var gear: Coordinate? = null
) {
    fun isAdjacent(symbolCoordinate: List<Coordinate>): Boolean {
        val f = firstDigit
        val l = lastDigit

        if (f == null || l == null) {
            return false
        }

        return symbolCoordinate.any { symbol ->
            val adjacent = (symbol.x - f.x).absoluteValue <= 1 && (symbol.y - f.y).absoluteValue <= 1 ||
                    (symbol.x - l.x).absoluteValue <= 1 && (symbol.y - l.y).absoluteValue <= 1

            if (adjacent) {
                gear = symbol
            }

            adjacent
        }
    }
}