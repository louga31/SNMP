package manager;

import common.TrapListener;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class HierarchicalManager extends TrapListenerImpl {
    private final List<TrapListener> childManagers = new ArrayList<>();

    protected HierarchicalManager() throws RemoteException {
        super();
    }

    public void addChildManager(TrapListener manager) {
        childManagers.add(manager);
    }

    public void removeChildManager(TrapListener manager) {
        childManagers.remove(manager);
    }

    @Override
    public void notify(String message) throws RemoteException {
        super.notify(message);
        for (TrapListener childManager : childManagers) {
            childManager.notify(message);
        }
    }
}