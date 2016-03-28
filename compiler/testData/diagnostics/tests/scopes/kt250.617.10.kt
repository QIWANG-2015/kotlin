package kt_250_617_10

import java.util.ArrayList
import java.util.HashMap

//KT-250 Incorrect variable resolve in constructor arguments of superclass
open class A(val x: Int)
class B(<!UNUSED_PARAMETER!>y<!>: Int) : A(<!INSTANCE_ACCESS_BEFORE_SUPER_CALL!>x<!>)  //x is resolved as a property in a, so no error is generated

//KT-617 Prohibit dollars in call to superclass constructors
open class M(<!UNUSED_PARAMETER!>p<!>: Int)
class N(val p: Int) : A(<!SYNTAX!><!SYNTAX!><!>$p<!><!SYNTAX!>)<!>

//KT-10 Don't allow to use properties in supertype initializers
open class Element()
class TextElement(<!UNUSED_PARAMETER!>name<!>: String) : Element()

abstract class Tag(val name : String) {
  val children = ArrayList<Element>()
  val attributes = HashMap<String, String>()
}

abstract class TagWithText(name : String) : Tag(name) {
  operator fun String.unaryPlus() {
    children.add(TextElement(this))
  }
}

open class BodyTag(name : String) : TagWithText(name) {
}

class Body() : BodyTag(<!INSTANCE_ACCESS_BEFORE_SUPER_CALL!>name<!>) { // Must be an error!
}
class Body1() : BodyTag(<!INSTANCE_ACCESS_BEFORE_SUPER_CALL!>this<!>.name) { // Must be an error!
}

//more tests

open class X(<!UNUSED_PARAMETER!>p<!>: Int, <!UNUSED_PARAMETER!>r<!>: Int) {
    val s = "s"
}

class Y(i: Int) : X(i, <!INSTANCE_ACCESS_BEFORE_SUPER_CALL, UNINITIALIZED_VARIABLE!>rrr<!>) {
    val rrr = 3
}

class Z(val i: Int) : X(<!INSTANCE_ACCESS_BEFORE_SUPER_CALL, TYPE_MISMATCH!>s<!>, <!INSTANCE_ACCESS_BEFORE_SUPER_CALL, UNINITIALIZED_VARIABLE!>x<!>) {
    val x = 2
}