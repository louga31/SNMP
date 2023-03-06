package manager;

import common.TrapListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TrapListenerImpl extends UnicastRemoteObject implements TrapListener {
    protected TrapListenerImpl() throws RemoteException {
        super();
    }

    @Override
    public void notify(String message) throws RemoteException {
        System.out.println("Received trap: " + message);
    }
}
