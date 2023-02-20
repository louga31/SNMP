package mib;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class MIB implements Serializable {
    private HashMap<String, MIBEntry> entries;
    private String deviceName;

    // Helper method to add default entries to the MIB
    private void addEntry(String oid, String name, String value) {
        entries.put(oid, new MIBEntry(oid, name, value));
    }

    private void addArrayEntry(String oid, String name, String[] value) {
        for (int i = 0; i < value.length; i++) {
            addEntry(oid + "." + i, name + i, value[i]);
        }
    }

    public MIB(String deviceName) {
        this.deviceName = deviceName;
        entries = new HashMap<>();
        // TODO: Default entries
        addEntry("1.3.6.1.2.1.1.4.0", "address", "127.0.0.1");
        addArrayEntry("1.2", "test", new String[]{"a", "b", "c"});
    }

    public boolean containsKey(String oid) {
        return entries.containsKey(oid);
    }

    public MIBEntry[] getAllEntries() {
        return entries.values().toArray(new MIBEntry[0]);
    }

    public MIBEntry get(String oid) {
        return entries.get(oid);
    }

    public void saveToFile() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(deviceName + ".mib");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
    }

    public static MIB loadFromFile(String deviceName) throws IOException, ClassNotFoundException {
        File file = new File(deviceName + ".mib");
        if (!file.exists()) {
            MIB mib = new MIB(deviceName);
            mib.saveToFile();
            return mib;
        }

        FileInputStream fileIn = new FileInputStream(deviceName + ".mib");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        MIB mib = (MIB) in.readObject();
        in.close();
        return mib;
    }
}
