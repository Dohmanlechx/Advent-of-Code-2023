import kotlin.math.pow

fun main(args: Array<String>) {
    d4a()
    d4b()
}

fun d4a() {
    val cards = generateCardsFromInput()
    val points = cards.fold(0) { acc, card ->
        acc + 2.0.pow(card.matchingNumbers().count() - 1).toInt()
    }
    require(points == 27059)
}

fun d4b() {
    // Bad solution :) Very slow compile, like 3-5 minutes
    val cards = generateCardsFromInput()
    val cardsToCheck = cards.toMutableList()
    var cardCount = cardsToCheck.count()

    while (cardsToCheck.isNotEmpty()) {
        val card = cardsToCheck.first()
        val matching = card.matchingNumbers().count()

        if (matching <= 0) {
            cardsToCheck.removeFirst()
        } else {
            cardCount += matching
            val newCardsToAdd = cards.drop(card.number).take(matching)
            cardsToCheck.addAll(newCardsToAdd)
            cardsToCheck.removeFirst()
        }
    }
    require(cardCount == 5744979)
}

data class Card(
    val index: Int,
    val number: Int,
    val winningNumbers: List<Int>,
    val playingNumbers: List<Int>,
) {
    fun matchingNumbers(): List<Int> {
        return winningNumbers.filter { it in playingNumbers }
    }
}

fun generateCardsFromInput(): List<Card> {
    return Reader.input(4).map { line ->
        val number = line.substringAfter("Card ").substringBefore(": ").toInt()
        val winningNumbers =
            line.substringAfter(": ").substringBefore(" | ").split(" ").filterNot(String::isEmpty).map(String::toInt)
        val playingNumbers = line.substringAfter(" | ").split(" ").filterNot(String::isEmpty).map(String::toInt)

        Card(number - 1, number, winningNumbers, playingNumbers)
    }
}
