public class C {
    public val foo: Int = 0

    public fun bar() {}

}

open class D {
    protected open fun willRemainProtected() {
    }

    protected open fun willBecomePublic() {
    }
}

class E : D() {
    protected override fun willRemainProtected() {
    }

    public override fun willBecomePublic() {
    }
}

class F {
    protected val foo: Int = 0

    protected fun bar() {}

    protected class Nested
}

class G {
    interface H {
        protected val foo: Int = 0

        protected fun bar() {}

        protected class Nested
    }
}
