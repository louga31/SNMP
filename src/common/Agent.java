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
}