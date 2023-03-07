package agent;

import common.AgentInterface;
import common.TrapListener;
import mib.*;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.HashSet;

public class Agent extends UnicastRemoteObject implements AgentInterface {
    private final MIB mib;
    private final HashMap<String, HashSet<Tuple<String, TrapListener>>> listeners = new HashMap<>();

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

    @Override
    public void subscribe(TrapListener listener, String oid, String communityString) throws RemoteException {
        this.get(oid, communityString); // Check if OID exists (throws exception if not) and if communityString is authorized

        if (!this.listeners.containsKey(oid)) {
            this.listeners.put(oid, new HashSet<>());
        }
        this.listeners.get(oid).add(new Tuple<>(communityString, listener));
    }

    @Override
    public void unsubscribe(TrapListener listener, String oid) throws RemoteException {
        if (this.listeners.containsKey(oid)) {
            for (Tuple<String, TrapListener> tuple : this.listeners.get(oid)) {
                if (tuple.y.equals(listener)) {
                    this.listeners.get(oid).remove(tuple);
                    break;
                }
            }
        }
    }

    @Override
    public MIBEntry get_next(String oid, String communityString) throws RemoteException {
        int value = Character.getNumericValue(oid.charAt(oid.length() - 1)) +1; // Valeur de fin de l'OID +1
        String oidPart1 = oid.substring(0, oid.length() - 2);
        MIBEntry entry = this.get(oidPart1 + "." + value, communityString); // On récupère l'OID suivant
        if (entry == null) {
            return this.get(oidPart1 + "." + 0, communityString); // Si l'OID suivant n'existe pas, on retourne l'OID 0
        }
        return entry;
    }

    public void sendTrap(String message, String oid) throws RemoteException {
        if (!this.listeners.containsKey(oid)) {
            return;
        }

        HashMap<String, HashSet<TrapListener>> toRemove = new HashMap<>();

        for (Tuple<String, TrapListener> tuple : listeners.get(oid)) {
            TrapListener listener = tuple.y;
            try {
                listener.notify("OID: " + oid + " - " + message + " - Data: " + this.get(oid, tuple.x).getValue());
            } catch (ConnectException e) {
                // Listener is no longer available and did not unsubscribe
                System.out.println("Listener is no longer available and did not unsubscribe, unsubscribing...");
                if (!toRemove.containsKey(oid)) {
                    toRemove.put(oid, new HashSet<>());
                }
                toRemove.get(oid).add(listener);
            }
        }

        for (String oidToRemove : toRemove.keySet()) {
            for (TrapListener listener : toRemove.get(oidToRemove)) {
                this.unsubscribe(listener, oidToRemove);
            }
        }
    }
}