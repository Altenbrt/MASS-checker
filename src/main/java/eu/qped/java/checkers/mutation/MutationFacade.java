package eu.qped.java.checkers.mutation;

public class MutationFacade {

    private final byte[] byteCode;
    private final String className;

    private final Class aClass;

    public MutationFacade(byte[] byteCode, String className, Class aClass) {
        this.byteCode = byteCode;
        this.className = className;
        this.aClass = aClass;
    }

    public String className() {
        return className;
    }

    public byte[] byteCode() {
        return byteCode;
    }

    public Class getaClass() {
        return aClass;
    }
}
