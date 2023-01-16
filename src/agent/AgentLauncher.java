package agent;

import common.Agent;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AgentLauncher {
    public static void main(String[] args) {
        try {
            Agent agent = new Agent();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Agent", agent);
            System.out.println("Agent ready.");
        } catch (Exception e) {
            System.err.println("Agent exception: " + e);
            e.printStackTrace();
        }
    }
}