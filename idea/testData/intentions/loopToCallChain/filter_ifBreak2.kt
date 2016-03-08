// WITH_RUNTIME
fun foo(list: List<String>): String? {
    <caret>for (s in list) {
        if (!s.isEmpty()) break
        return s
    }
    return null
}