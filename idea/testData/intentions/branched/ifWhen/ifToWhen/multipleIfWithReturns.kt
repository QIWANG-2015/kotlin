fun foo(arg: Any?): Int {
    // 1
    <caret>if (arg is Int) {
        return arg // 2
    }
    // 3: DISAPPEARS
    if (arg is String) {
        return 42 // 4
    }
    // 5: DISAPPEARS
    if (arg == null) {
        // 6
        return 0
    }
    // 7: DISAPPEARS
    return -1 // 8
}