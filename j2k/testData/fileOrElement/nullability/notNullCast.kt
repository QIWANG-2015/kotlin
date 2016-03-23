class Passenger {
    open class PassParent

    class PassChild : PassParent()

    fun provideNullable(p: Int): PassParent? {
        return if (p > 0) PassChild() else null
    }

    fun context() {
        val pass = provideNullable(1)!!
        accept(pass as PassChild)

        val pass2 = provideNullable(1)
        if (1 == 2) {
            assert(pass != null)
            accept(pass2 as PassChild)
        }
        accept(pass2 as PassChild)
    }

    fun accept(p: PassChild) {
    }
}