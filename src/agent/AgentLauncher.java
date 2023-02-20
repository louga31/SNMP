package agent;

import common.Agent;
import mib.MIB;
import mib.MIBEntry;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class AgentLauncher {
    private static final int RMI_PORT = 1099;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AgentLauncher <deviceName>");
            System.exit(1);
        }

        String deviceName = args[0];

        try {
            // Load or create the MIB
            MIB mib = MIB.loadFromFile(deviceName);
            if (mib == null) {
                mib = new MIB(deviceName);
            }

            // Create the agent
            Agent agent = new Agent(deviceName, mib);

            // Register the agent with the RMI registry
            String url = "rmi://localhost:" + RMI_PORT + "/Agent_" + deviceName;
            try {
                Naming.rebind(url, agent);
            } catch (Exception e) {
                LocateRegistry.createRegistry(RMI_PORT);
                Naming.rebind(url, agent);
            }

            System.out.println("Agent launched for device " + deviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
