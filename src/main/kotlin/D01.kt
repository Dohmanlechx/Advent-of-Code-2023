fun main(args: Array<String>) {
    p1()
    p2()
}

fun p1() {
    val lines = Reader.input(1)
    var sum = 0

    lines.forEach { line ->
        val digits = line.filter(Char::isDigit)
        val number = digits.convertFirstLastToNumber()
        sum += number
    }

    assert(sum == 54927)
}

fun p2() {
    val map = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )

    val lines = Reader.input(1)
    var sum = 0

    lines.forEach { line ->
        var stringOfNumbers = ""

        for (i in line.indices) {
            val segment = line.substring(i)

            if (segment.first().isDigit()) {
                stringOfNumbers += segment.first()
            } else {
                for ((k, v) in map) {
                    if (segment.startsWith(k)) {
                        stringOfNumbers += v
                        break
                    }
                }
            }
        }

        val number = stringOfNumbers.convertFirstLastToNumber()
        sum += number
    }

    assert(sum == 54581)
}

fun String.convertFirstLastToNumber() = "${first()}${last()}".toInt()