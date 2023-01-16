package manager;

import agent.AgentInterface;

import java.rmi.*;

public class Manager {
    public static void main(String[] args) {
        try {
            AgentInterface agent = (AgentInterface) Naming.lookup("rmi://localhost/Agent");
            String name = agent.getName();
            System.out.println("Current name: " + name);
            agent.setName("New name");
            System.out.println("Name updated to: " + agent.getName());
        } catch (Exception e) {
            System.err.println("Manager exception: " + e);
            e.printStackTrace();
        }
    }
}