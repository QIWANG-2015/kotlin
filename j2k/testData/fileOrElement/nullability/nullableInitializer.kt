class PlainTrain {
    fun nullableString(p: Int): String? {
        return if (p > 0) "response" else null
    }

    fun notNullString(p: Int): String {
        return "response"
    }

    fun nullableObj(p: Int): Any? {
        return if (p > 0) "response" else null
    }

    val nullableInitializerField = nullableString(3)
    val nullableInitializerFieldCast = nullableObj(3) as String?

    fun testProperty() {
        nullableInitializerField!![0]
        nullableInitializerFieldCast!![0]
    }

    fun testLocalVariable() {
        val nullableInitializerVal = nullableString(3)
        val nullableInitializerValCast = nullableObj(3) as String?

        nullableInitializerVal!![0]
        nullableInitializerValCast!![0]
    }

    val myNotNullField = notNullString(1)

    var notNullInitializerFieldNullableUsage: String? = myNotNullField
    var notNullInitializerFieldNotNullUsage = myNotNullField

    var nullInitializerFieldNullableUsage: String? = null
    var nullInitializerFieldNotNullUsage: String? = null

    fun testNotNull(obj: Any?) {
        if (true) {
            notNullInitializerFieldNullableUsage = obj as String?
            notNullInitializerFieldNotNullUsage = "str"
        } else {
            nullInitializerFieldNullableUsage = obj as String?
            nullInitializerFieldNotNullUsage = "str"
        }
    }
}