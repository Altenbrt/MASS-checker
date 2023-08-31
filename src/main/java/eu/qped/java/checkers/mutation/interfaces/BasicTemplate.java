package eu.qped.java.checkers.mutation.interfaces;

public abstract class BasicTemplate implements BasicTemplateStructure{
    Versions activeVersion;


    public void configuration(Versions v) {
        this.activeVersion = v;
    }

    public Versions getActiveVersion() {
        return activeVersion;
    }
}
