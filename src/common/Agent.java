package common;

import java.rmi.*;
import java.rmi.server.*;

public class Agent extends UnicastRemoteObject implements AgentInterface {
    private String name;
    private String address;

    public Agent(String name, String address) throws RemoteException {
        this.name = name;
        this.address = address;
    }

    public String getName() throws RemoteException {
        return name;
    }

    public void setName(String newName) throws RemoteException {
        try {
            Naming.unbind("rmi://localhost:1099/Agent_" + name);
            Naming.rebind("rmi://localhost:1099/Agent_" + newName, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        name = newName;
    }

    public String getAddress() throws RemoteException {
        return address;
    }

    public void setAddress(String address) throws RemoteException {
        this.address = address;
    }
}