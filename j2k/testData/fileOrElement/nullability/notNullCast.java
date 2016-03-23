public class Passenger {
    public static class PassParent {
    }

    public static class PassChild extends PassParent {
    }

    public PassParent provideNullable(int p) {
        return p > 0 ? new PassChild() : null;
    }

    public void context() {
        PassParent pass = provideNullable(1);
        assert pass != null;
        accept((PassChild) pass);

        PassParent pass2 = provideNullable(1);
        if (1 == 2) {
            assert pass != null;
            accept((PassChild) pass2);
        }
        accept((PassChild) pass2);
    }

    public void accept(PassChild p) {
    }
}