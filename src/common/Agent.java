package common;

import mib.MIB;
import mib.MIBEntry;

import java.rmi.*;
import java.rmi.server.*;

public class Agent extends UnicastRemoteObject implements AgentInterface {
    private String deviceName;
    private MIB mib;
    public Agent(String deviceName, MIB mib) throws RemoteException {
        this.deviceName = deviceName;
        this.mib = mib;
    }

    @Override
    public MIBEntry get(String oid) throws RemoteException {
        if (!mib.containsKey(oid)) {
            return null;
        }

        return mib.get(oid);
    }

    @Override
    public void set(String oid, String value) throws RemoteException {
        mib.get(oid).setValue(value);
        try {
            mib.saveToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MIBEntry get_next(String oid) throws RemoteException {
        int value = (int) Character.getNumericValue(oid.charAt(oid.length() - 1)) +1; // Valeur de fin de l'OID +1
        MIBEntry entry = this.get(oid.substring(0,oid.length()-2) + "." + value); // On récupère l'OID suivant
        if (entry == null) {
            return this.get(oid.substring(0,oid.length()-2) + "." + 0); // Si l'OID suivant n'existe pas, on retourne l'OID 0
        }
        return entry;
    }
}