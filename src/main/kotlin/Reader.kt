object Reader {
    fun input(day: Int, split: String = "\n") =
        lines("/${day.padded()}/input", split)

    fun example(day: Int, split: String = "\n") =
        lines("/${day.padded()}/example", split)

    private fun lines(path: String, split: String) =
        this@Reader::class.java
            .getResource(path)!!
            .readText()
            .split(split)
            .map(String::trim)

    private fun Int.padded() =
        toString().padStart(2, '0')
}