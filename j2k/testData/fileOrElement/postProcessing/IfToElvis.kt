internal class C {
    fun foo1(s: String?): String {
        return s ?: ""
    }

    fun foo2(s: String?): String? {
        return if (s == null) s else ""
    }

    fun foo3(s: String?): String? {
        return if (s != null) "" else s
    }

    fun foo4(s: String?): String {
        return s ?: ""
    }

    fun foo5(s: String?): String {
        return s ?: ""
    }

    fun foo6(s: String?): String? {
        return s ?: s
    }
}