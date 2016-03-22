import org.jetbrains.annotations.Nullable;

public class PlainTrain {
    public String nullableString(int p) {
        return p > 0 ? "response" : null;
    }

    public String notNullString(int p) {
        return "response";
    }

    public Object nullableObj(int p) {
        return p > 0 ? "response" : null;
    }

    public final String nullableInitializerField = nullableString(3);
    public final String nullableInitializerFieldCast = (String) nullableObj(3);

    public void testProperty() {
        nullableInitializerField.charAt(0)
        nullableInitializerFieldCast.charAt(0)
    }

    public void testLocalVariable() {
        String nullableInitializerVal = nullableString(3);
        String nullableInitializerValCast = (String) nullableObj(3);

        nullableInitializerVal.charAt(0)
        nullableInitializerValCast.charAt(0)
    }

    public final String myNotNullField = notNullString(1);

    public String notNullInitializerFieldNullableUsage = myNotNullField;
    public String notNullInitializerFieldNotNullUsage = myNotNullField;

    public String nullInitializerFieldNullableUsage = null;
    public String nullInitializerFieldNotNullUsage = null;

    public void testNotNull(@Nullable Object obj) {
        if (true) {
            notNullInitializerFieldNullableUsage = (String) obj;
            notNullInitializerFieldNotNullUsage = "str";
        }
        else {
            nullInitializerFieldNullableUsage = (String) obj;
            nullInitializerFieldNotNullUsage = "str";
        }
    }
}