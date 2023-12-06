fun main(args: Array<String>) {
    d6a()
    d6b()
}

fun d6a() {
    val times = Reader.input(6)[0].times().map(String::toLong)
    val distances = Reader.input(6)[1].distances().map(String::toLong)
    val races = List(size = 4) { Race(times[it], distances[it]) }
    val sum = races.fold(1L) { acc, race -> acc * race.winWays() }
    require(sum == 861300L)
}

fun d6b() {
    val time = Reader.input(6)[0].timesFlattened()
    val distance = Reader.input(6)[1].distancesFlattened()
    val sum = Race(time, distance).winWays()
    require(sum == 28101347L)
}

data class Race(val time: Long, val record: Long) {
    fun winWays(): Long {
        var count = 0L
        for (speed in 0..<time) {
            val remainder = time - speed
            val distance = remainder * speed
            if (distance > record) {
                count++
            }
        }
        return count
    }
}

fun String.times() = substringAfter(": ").substringBefore(",").split(" ")
fun String.distances() = substringAfter("Distance: ").split(" ")
fun String.timesFlattened() = times().join()
fun String.distancesFlattened() = distances().join()
fun List<String>.join() = joinToString(" ").replace(" ", "").let(String::toLong)

