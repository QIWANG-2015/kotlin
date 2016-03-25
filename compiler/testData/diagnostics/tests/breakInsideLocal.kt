fun test() {
    while (true) {
        fun local1() {
            <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>break<!>
        }
    }
}

fun test2() {
    while (true) {
        <!UNUSED_LAMBDA_EXPRESSION!>{
            <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>continue<!>
        }<!>
    }
}

fun test3() {
    while (true) {
        class LocalClass {
            init {
                <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>continue<!>
            }

            fun foo() {
                <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>break<!>
            }
        }
    }
}

fun test4() {
    while (true) {
        object: Any() {
            init {
                <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>break<!>
            }
        }
    }
}