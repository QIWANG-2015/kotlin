class C {
    String foo1(String s) {
        return s != null ? s : "";
    }

    String foo2(String s) {
        return s == null ? s : "";
    }

    String foo3(String s) {
        return s != null ? "" : s;
    }

    String foo4(String s) {
        return s == null ? "" : s;
    }

    String foo5(String s) {
        return null == s ? "" : s;
    }

    String foo6(String s) {
        return null == s ? s : s;
    }
}