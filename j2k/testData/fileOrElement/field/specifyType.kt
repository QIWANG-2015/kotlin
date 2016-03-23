import java.util.*

internal class A {
    private val field1 = ArrayList<String>()
    val field2: List<String> = ArrayList()
    val field3 = 0
    protected val field4 = 0

    private var field5: List<String> = ArrayList()
    var field6: List<String> = ArrayList()

    private var field7 = 0
    var field8 = 0

    private var field9: String? = "a"
    private var field10: String? = foo()

    fun foo(): String {
        return "x"
    }

    fun bar() {
        field5 = ArrayList<String>()
        field7++
        field8++
        field9 = null
        field10 = null
    }

    internal interface I

    private val anonymous = object : I {

    }
    var anonymous2: I = object : I {

    }

    private var iimpl: I = anonymous
    var iimpl2: I = anonymous

    fun testAnonymousObject(i: Any) {
        if (true) {
            iimpl = i as I
        } else {
            iimpl2 = i as I
        }
    }
}