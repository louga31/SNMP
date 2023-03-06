package common;

import mib.MIBEntry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgentInterface extends Remote {
    MIBEntry get(String oid, String communityString) throws RemoteException;
    void set(String oid, String value, String communityString) throws RemoteException;
    MIBEntry get_next(String oid, String communityString) throws RemoteException;
    void subscribe(TrapListener listener) throws RemoteException;
    void unsubscribe(TrapListener listener) throws RemoteException;
}