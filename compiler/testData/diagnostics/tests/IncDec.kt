class IncDec() {
  operator fun inc() : IncDec = this
  operator fun dec() : IncDec = this
}

fun testIncDec() {
  var x = IncDec()
  x++
  ++x
  x--
  --x
  x = <!UNUSED_CHANGED_VALUE!>x++<!>
  x = <!UNUSED_CHANGED_VALUE!>x--<!>
  x = ++x
  <!UNUSED_VALUE!>x =<!> --x
}

class WrongIncDec() {
  operator fun inc() : Int = 1
  operator fun dec() : Int = 1
}

fun testWrongIncDec() {
  var x = WrongIncDec()
  x<!RESULT_TYPE_MISMATCH!>++<!>
  <!RESULT_TYPE_MISMATCH!>++<!>x
  x<!RESULT_TYPE_MISMATCH!>--<!>
  <!RESULT_TYPE_MISMATCH!>--<!>x
}

class UnitIncDec() {
  operator fun inc() : Unit {}
  operator fun dec() : Unit {}
}

fun testUnitIncDec() {
  var x = UnitIncDec()
  x<!INC_DEC_SHOULD_NOT_RETURN_UNIT!>++<!>
  <!INC_DEC_SHOULD_NOT_RETURN_UNIT!>++<!>x
  x<!INC_DEC_SHOULD_NOT_RETURN_UNIT!>--<!>
  <!INC_DEC_SHOULD_NOT_RETURN_UNIT!>--<!>x
  x = <!UNUSED_CHANGED_VALUE!>x<!INC_DEC_SHOULD_NOT_RETURN_UNIT!>++<!><!>
  x = <!UNUSED_CHANGED_VALUE!>x<!INC_DEC_SHOULD_NOT_RETURN_UNIT!>--<!><!>
  x = <!INC_DEC_SHOULD_NOT_RETURN_UNIT!>++<!>x
  <!UNUSED_VALUE!>x =<!> <!INC_DEC_SHOULD_NOT_RETURN_UNIT!>--<!>x
}
