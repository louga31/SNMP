package manager;

import common.AgentInterface;
import mib.MIBEntry;

import java.rmi.*;
import java.util.Objects;
import java.util.Scanner;

public class Manager {

    public static void main(String[] args) {
        try {
            TrapListenerImpl trapListener = new TrapListenerImpl();

            String agentName;
            AgentInterface agent;

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter your community string:");
            String communityString = sc.nextLine();
            while (true) {
                System.out.println("Enter the name of the agent you want to manage (or exit to quit):");
                agentName = sc.nextLine().split(" ")[0];
                if (agentName.equals("exit")) {
                    break;
                }

                try {
                    agent = (AgentInterface) Naming.lookup("rmi://localhost/Agent_" + agentName);
                    System.out.println("Successfully connected to agent: " + agentName);

                    String command;

                    while (true) {
                        System.out.println("Enter a command (disconnect to quit):");
                        command = sc.nextLine();
                        if (command.equals("disconnect")) {
                            break;
                        }

                        String[] commandParts = command.split(" ");
                        if (commandParts.length != 2) {
                            System.out.println("Invalid command");
                            continue;
                        }

                        switch (commandParts[0].toLowerCase()) {
                            case "set" -> {
                                String oid = commandParts[1];
                                System.out.println("Enter value:");
                                String value = sc.nextLine();
                                agent.set(oid, value, communityString);
                            }

                            case "get" -> {
                                String oid = commandParts[1];
                                System.out.println(agent.get(oid, communityString));
                            }

                            case "get-next" -> {
                                String oid = commandParts[1];
                                System.out.println(agent.get_next(oid, communityString));
                            }

                            case "walk" -> {
                                String oid = commandParts[1];
                                MIBEntry entry = agent.get(oid + ".0", communityString);
                                System.out.println(entry);
                                while (!Objects.isNull(entry) && !agent.get_next(entry.getOid(), communityString).getOid().equals(oid + ".0")) {
                                    entry = agent.get_next(entry.getOid(), communityString);
                                    if (!Objects.isNull(entry)) {
                                        System.out.println(entry);
                                    }
                                }
                            }

                            case "subscribe" -> {
                                String oid = commandParts[1];
                                agent.subscribe(trapListener, oid, communityString);
                                System.out.println("Subscribed to " + oid);
                            }

                            case "unsubscribe" -> {
                                String oid = commandParts[1];
                                agent.unsubscribe(trapListener, oid);
                                System.out.println("Unsubscribed from " + oid);
                            }

                            default -> System.out.println("Invalid command");
                        }
                    }

                } catch (NotBoundException | RemoteException e) {
                    System.err.println("Could not connect to agent: " + agentName + ". Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Manager exception: " + e);
            e.printStackTrace();
        }
    }
}