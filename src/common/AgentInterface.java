package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgentInterface extends Remote {
    String getName() throws RemoteException;
    void setName(String name) throws RemoteException;
    String getAddress() throws RemoteException;
    void setAddress(String address) throws RemoteException;
}