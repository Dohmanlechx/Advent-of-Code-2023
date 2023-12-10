typealias Hand = List<Char>

fun main(args: Array<String>) {
    d7a()
    d7b()
}

fun d7a() {
    val input = Reader.input(7)
    val games = mutableListOf<Game>()

    for (line in input) {
        val hand = line.substringBefore(" ").map { c -> c }
        val bid = line.substringAfter(" ").toInt()
        games.add(Game(hand, bid))
    }

    // Map by points
    val m = mutableMapOf<Int, MutableList<Game>>()

    for (game in games) {
        val h = game.hand
        val b = game.bid
        val p = CamelCards.calculatePoints(h)

        if (!m.containsKey(p)) {
            m[p] = mutableListOf(Game(h, b))
        } else {
            var next: Int? = null

            for (i in m[p]!!.indices) {
                val other = m[p]?.getOrNull(i)
                if (other != null && h.winsAgainst(other.hand)) {
                    next = i
                    break
                }
            }

            m[p]!!.add(next ?: m[p]!!.size, Game(h, b))
        }
    }

    val ranks = m.toSortedMap()
        .values
        .flatMap { it.asReversed() }
        .toMutableList()

    val sum = ranks.foldIndexed(0) { i, acc, game ->
        acc + (game.bid * (i + 1))
    }

    require(sum == 253954294)
}

fun d7b() {

}

data class Game(val hand: Hand, val bid: Int)

fun Hand.winsAgainst(other: Hand): Boolean {
    val a = CamelCards.calculatePoints(this)
    val b = CamelCards.calculatePoints(other)
    return if (a > b) true else if (a < b) false else CamelCards.calculateHighCard(this, other)
}

object CamelCards {
    private const val FIVE_OF_A_KIND = 7
    private const val FOUR_OF_A_KIND = 6
    private const val FULL_HOUSE = 5
    private const val THREE_OF_A_KIND = 4
    private const val TWO_PAIR = 3
    private const val ONE_PAIR = 2
    private const val HIGH_CARD = 1

    fun calculatePoints(hand: Hand): Int {
        val set = hand.toSet()
        return when (set.count()) {
            5 -> HIGH_CARD
            4 -> ONE_PAIR
            3 -> {
                val group = hand.groupBy { it }.maxByOrNull { it.value.size }!!
                return when (group.value.size) {
                    3 -> THREE_OF_A_KIND
                    else -> TWO_PAIR
                }
            }

            2 -> {
                val group = hand.groupBy { it }.maxByOrNull { it.value.size }!!
                return when (group.value.size) {
                    4 -> FOUR_OF_A_KIND
                    else -> FULL_HOUSE
                }
            }

            else -> FIVE_OF_A_KIND
        }
    }

    fun calculateHighCard(a: Hand, b: Hand): Boolean {
        for (i in 0..4) {
            if (a[i].strength() > b[i].strength()) return true
            if (b[i].strength() > a[i].strength()) return false
            else continue
        }
        throw Exception("There must be a winner")
    }
}

fun Char.strength(): Int {
    val m = mapOf('T' to 10, 'J' to 11, 'Q' to 12, 'K' to 13, 'A' to 14)
    return m[this] ?: this.digitToInt()
}