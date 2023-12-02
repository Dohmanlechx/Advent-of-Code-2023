import kotlin.math.max

fun main(args: Array<String>) {
    d2a()
    d2b()
}

fun d2a() {
    val games = Reader.input(2)
    val possibleGameIndices = (1..games.count()).toMutableList()

    for (game in games) {
        val gameIndex = game.substringAfter("Game ").substringBefore(":").toInt()
        val sets = game.substringAfter(":").split(";")

        for (set in sets) {
            for (color in RGB.entries) {
                val cubes = set.split(", ")
                if (cubes.countOfColor(color) > color.limit) {
                    possibleGameIndices.remove(gameIndex)
                    break
                }
            }
        }
    }

    val sum = possibleGameIndices.sum()
    require(sum == 2377)
}

fun d2b() {
    val games = Reader.input(2)
    var sum = 0

    for (game in games) {
        val sets = game.substringAfter(":").split(";")

        var r = 0
        var g = 0
        var b = 0

        for (set in sets) {
            val cubes = set.split(", ")
            r = max(r, cubes.countOfColor(RGB.RED))
            g = max(g, cubes.countOfColor(RGB.GREEN))
            b = max(b, cubes.countOfColor(RGB.BLUE))
        }

        sum += (r * g * b)
    }

    require(sum == 71220)
}

enum class RGB(val limit: Int) {
    RED(12), GREEN(13), BLUE(14)
}

fun List<String>.countOfColor(color: RGB): Int {
    val name = color.name.lowercase()
    return find { it.contains(name) }?.substringBefore(" $name")?.trim()?.toInt() ?: 0
}