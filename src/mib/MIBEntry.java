package mib;

import java.io.Serializable;

public class MIBEntry implements Serializable {
    private String oid;
    private String name;
    private String value;

    public MIBEntry(String oid, String name, String value) {
        this.oid = oid;
        this.name = name;
        this.value = value;
    }

    public String getOid() {
        return oid;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "OID: " + oid + "\nName: " + name + "\nValue: " + value;
    }
}
