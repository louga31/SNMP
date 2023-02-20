package common;

import mib.MIBEntry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgentInterface extends Remote {
    MIBEntry get(String oid) throws RemoteException;
    void set(String oid, String value) throws RemoteException;
    MIBEntry get_next(String oid) throws RemoteException;
}