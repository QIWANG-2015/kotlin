class B () {}

open class A(val b : B) {
    fun a(): A = object: A(this@A.b) {}
}

fun box() : String {
    A(B()).a()
    return "OK"
}
