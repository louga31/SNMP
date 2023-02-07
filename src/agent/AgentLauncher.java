package agent;

import common.Agent;

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

            // Start the RMI registry
            Registry registry = LocateRegistry.getRegistry(RMI_PORT);

            // Register the agent with the RMI registry
            String url = "rmi://localhost:" + RMI_PORT + "/Agent_" + deviceName;
            try {
                registry.rebind(url, agent);
            } catch (Exception e) {
                registry = LocateRegistry.createRegistry(RMI_PORT);
                registry.rebind(url, agent);
            }


            System.out.println("Agent launched for device " + deviceName + " at address " + deviceAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
