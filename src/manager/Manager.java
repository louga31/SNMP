package manager;

import common.AgentInterface;
import mib.MIBEntry;

import java.rmi.*;
import java.util.Objects;
import java.util.Scanner;

public class Manager {
    public static void main(String[] args) {
        try {
            String agentName = "";
            AgentInterface agent;

            Scanner sc = new Scanner(System.in);
            while (!agentName.equals("exit")) {
                System.out.println("Enter the name of the agent you want to manage (or exit to quit):");
                agentName = sc.nextLine();
                if (agentName.equals("exit")) {
                    break;
                }

                try {
                    agent = (AgentInterface) Naming.lookup("rmi://localhost/Agent_" + agentName);
                    System.out.println("Successfully connected to agent: " + agentName);

                    String command = "";

                    while (!command.equals("disconnect")) {
                        System.out.println("Enter a command (disconnect to quit):");
                        command = sc.nextLine();
                        if (command.equals("disconnect")) {
                            break;
                        }

                        switch (command) {

                            case "set" -> {
                                System.out.println("Enter OID:");
                                String oid = sc.nextLine();
                                System.out.println("Enter value:");
                                String value = sc.nextLine();
                                agent.set(oid, value);
                            }

                            case "get" -> {
                                System.out.println("Enter OID:");
                                String oid = sc.nextLine();
                                System.out.println(agent.get(oid));
                            }

                            case "get-next" -> {
                                System.out.println("Enter OID:");
                                String oid = sc.nextLine();
                                System.out.println(agent.get_next(oid));
                            }

                            case "walk" -> {
                                System.out.println("Enter OID:");
                                String oid = sc.nextLine();
                                MIBEntry entry = agent.get(oid + ".0");
                                System.out.println(entry);
                                while (!Objects.isNull(entry) && !agent.get_next(entry.getOid()).getOid().equals(oid + ".0")) {
                                    entry = agent.get_next(entry.getOid());
                                    if (!Objects.isNull(entry)) {
                                        System.out.println(entry);
                                    }
                                }
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