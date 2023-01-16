package common;

import java.rmi.*;
import java.rmi.server.*;

public class Agent extends UnicastRemoteObject implements AgentInterface {
    private String name;
    private String address;

    public Agent() throws RemoteException {
        name = "Default name";
        address = "127.0.0.1";
    }

    public String getName() throws RemoteException {
        return name;
    }

    public void setName(String newName) throws RemoteException {
        name = newName;
    }

    public String getAddress() throws RemoteException {
        return address;
    }

    public void setAddress(String address) throws RemoteException {
        this.address = address;
    }
}