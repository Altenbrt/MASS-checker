package eu.qped.java.checkers.mutation.interfaces;

public abstract class StudentTests {
    BasicTemplate program;

    public StudentTests(BasicTemplate program) {
        this.program = program;
    }

    public void test(){
    }

    public BasicTemplate getProgram() {
        return program;
    }

}
