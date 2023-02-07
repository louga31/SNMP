package agent;

import common.Agent;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AgentLauncher {
    private static final int RMI_PORT = 1099;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java AgentLauncher <deviceName> <deviceAddress>");
            System.exit(1);
        }

        String deviceName = args[0];
        String deviceAddress = args[1];

        try {
            // Create the agent
            Agent agent = new Agent(deviceName, deviceAddress);

            // Register the agent with the RMI registry
            String url = "rmi://localhost:" + RMI_PORT + "/Agent_" + deviceName;
            try {
                Naming.rebind(url, agent);
            } catch (Exception e) {
                LocateRegistry.createRegistry(RMI_PORT);
                Naming.rebind(url, agent);
            }


            System.out.println("Agent launched for device " + deviceName + " at address " + deviceAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
