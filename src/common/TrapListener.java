package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrapListener extends Remote {
    void notify(String message) throws RemoteException;
}
