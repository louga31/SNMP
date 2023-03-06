package common;

import mib.*;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashSet;

public class Agent extends UnicastRemoteObject implements AgentInterface {
    private final MIB mib;
    private final HashSet<TrapListener> listeners = new HashSet<>();

    public Agent(MIB mib) throws RemoteException {
        this.mib = mib;
    }

    @Override
    public MIBEntry get(String oid, String communityString) throws RemoteException {
        return mib.get(oid, communityString);
    }

    @Override
    public void set(String oid, String value, String communityString) throws RemoteException {
        mib.set(oid, value, communityString);
    }

    public void subscribe(TrapListener listener) throws RemoteException {
        this.listeners.add(listener);
    }

    public void unsubscribe(TrapListener listener) throws RemoteException {
        this.listeners.remove(listener);
    }

    @Override
    public MIBEntry get_next(String oid, String communityString) throws RemoteException {
        int value = Character.getNumericValue(oid.charAt(oid.length() - 1)) +1; // Valeur de fin de l'OID +1
        MIBEntry entry = this.get(oid.substring(0,oid.length()-2) + "." + value, communityString); // On récupère l'OID suivant
        if (entry == null) {
            return this.get(oid.substring(0,oid.length()-2) + "." + 0, communityString); // Si l'OID suivant n'existe pas, on retourne l'OID 0
        }
        return entry;
    }

    public void sendTrap(String message) throws RemoteException {
        for (TrapListener listener : listeners) {
            try {
                listener.notify(message);
            } catch (RemoteException e) {
                // Listener is no longer available and did not unsubscribe
                e.printStackTrace();
                listeners.remove(listener);
            }
        }
    }
}