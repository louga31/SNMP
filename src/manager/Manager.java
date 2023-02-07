package manager;

import common.AgentInterface;

import java.rmi.*;
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
                            case "getName" -> System.out.println("Agent name: " + agent.getName());
                            case "setName" -> {
                                System.out.println("Enter the new name:");
                                String newName = sc.nextLine();
                                agent.setName(newName);
                                System.out.println("Agent name changed to: " + agent.getName());
                                command = "disconnect";
                            }
                            case "getAddress" -> System.out.println("Agent address: " + agent.getAddress());
                            case "setAddress" -> {
                                System.out.println("Enter the new address:");
                                String newAddress = sc.nextLine();
                                agent.setAddress(newAddress);
                                System.out.println("Agent address changed to: " + agent.getAddress());
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