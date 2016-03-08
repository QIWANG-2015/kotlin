// WITH_RUNTIME
fun foo(list: List<String>): Int? {
    <caret>for (s in list) {
        if (s.isEmpty()) break
        val l = s.length
        return l
    }
    return null
}