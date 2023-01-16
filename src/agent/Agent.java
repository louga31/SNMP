package agent;

import java.rmi.*;
import java.rmi.server.*;

public class Agent extends UnicastRemoteObject implements AgentInterface {

    private String name;

    public Agent() throws RemoteException {
        name = "Default name";
    }

    public String getName() throws RemoteException {
        return name;
    }

    public void setName(String newName) throws RemoteException {
        name = newName;
    }
}