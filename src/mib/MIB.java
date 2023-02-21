package mib;

import java.io.*;
import java.rmi.RemoteException;
import java.util.HashMap;

public class MIB implements Serializable {
    private final HashMap<String, MIBEntry> entries;
    private final String deviceName;
    private final Permission defaultPermission = new Permission("public", "private");

    // Helper method to add default entries to the MIB
    private void addEntry(String oid, String name, String value, AccessType accessType) {
        entries.put(oid, new MIBEntry(oid, name, value, accessType));
    }

    private void addArrayEntry(String oid, String name, String[] value, AccessType accessType) {
        for (int i = 0; i < value.length; i++) {
            addEntry(oid + "." + i, name + i, value[i], accessType);
        }
    }

    public MIB(String deviceName) {
        this.deviceName = deviceName;
        entries = new HashMap<>();
        // TODO: Default entries
        addEntry("1.1", "address", "127.0.0.1", AccessType.READ_WRITE);
        addEntry("1.2", "name", "default", AccessType.READ_WRITE);
        addArrayEntry("1.3", "process", new String[]{"1234", "1337", "4242"}, AccessType.READ_ONLY);
        addArrayEntry("1.4", "dns", new String[]{"1.1.1.1", "1.0.0.1"}, AccessType.READ_WRITE);
    }

    public MIBEntry get(String oid, String communityString) throws RemoteException {
       if (!entries.containsKey(oid)) {
           return null;
       }

         if (!defaultPermission.hasReadAccess(communityString)) {
              throw new RemoteException("Access denied");
         }

        MIBEntry entry = entries.get(oid);
        if (entry.getAccessType() == AccessType.NO_ACCESS) {
            throw new RemoteException("Access denied");
        }

        return entry;
    }

    public void set(String oid, String value, String communityString) throws RemoteException {
        if (!entries.containsKey(oid)) {
            throw new RemoteException("OID not found");
        }

        if (!defaultPermission.hasWriteAccess(communityString)) {
            throw new RemoteException("Access denied");
        }

        MIBEntry entry = entries.get(oid);
        if (entry.getAccessType() != AccessType.READ_WRITE) {
            throw new RemoteException("Access denied");
        }

        entry.setValue(value);
        try {
            saveToFile();
        } catch (IOException e) {
            throw new RemoteException("Error while saving MIB to file", e);
        }
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
