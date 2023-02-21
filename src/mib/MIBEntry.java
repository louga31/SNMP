package mib;

import java.io.Serializable;

public class MIBEntry implements Serializable {
    private final String oid;
    private final String name;
    private String value;
    private final AccessType accessType;

    public MIBEntry(String oid, String name, String value, AccessType accessType) {
        this.oid = oid;
        this.name = name;
        this.value = value;
        this.accessType = accessType;
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

    public AccessType getAccessType() {
        return accessType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "OID: " + oid + "\nName: " + name + "\nValue: " + value;
    }
}
