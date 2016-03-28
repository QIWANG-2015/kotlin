open class S(val a: Any, val b: Any, val c: Any) {}

object A : S(<!INSTANCE_ACCESS_BEFORE_SUPER_CALL, UNINITIALIZED_VARIABLE!>prop1<!>, <!INSTANCE_ACCESS_BEFORE_SUPER_CALL!>prop2<!>, <!INSTANCE_ACCESS_BEFORE_SUPER_CALL!>func<!>()) {
    val prop1 = 1
    val prop2: Int
        get() = 1
    fun func() {}
}
