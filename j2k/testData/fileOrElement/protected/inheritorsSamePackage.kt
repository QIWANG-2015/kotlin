package test

open class BaseInheritorSamePackage {

    fun foo() {

    }

    var i = 1
}

internal class DerivedInheritorSamePackage : BaseInheritorSamePackage() {
    fun usage1() {
        val base = BaseInheritorSamePackage()
        base.foo()
        val i = base.i
    }
}