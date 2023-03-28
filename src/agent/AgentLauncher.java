package agent;

import mib.MIB;

import java.io.InputStream;
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
            Agent agent = new Agent(mib);

            // Register the agent with the RMI registry
            String url = "rmi://localhost:" + RMI_PORT + "/Agent_" + deviceName;
            try {
                Naming.rebind(url, agent);
            } catch (Exception e) {
                LocateRegistry.createRegistry(RMI_PORT);
                Naming.rebind(url, agent);
            }

            System.out.println("Agent launched for device " + deviceName);
            System.out.println();

            int previousCpuUsage = -1;
            while(true) {
                // Trigger trap on enter key
                InputStream inputStream = System.in;
                if (inputStream.available() > 0 && inputStream.read() != ' ') {
                    agent.sendTrap("Hello from " + deviceName, "1.1");
                }

                // Trap CPU Usage Percentage
                int cpuUsage = Integer.parseInt(mib.get("1.5", "private").getValue().toString());
                if (cpuUsage > 70 && cpuUsage != previousCpuUsage) {
                    agent.sendTrap(deviceName + " - CPU Usage : ", "1.5");
                    previousCpuUsage = cpuUsage;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
