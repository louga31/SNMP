package manager;

import common.AgentInterface;

import java.rmi.*;

public class Manager {
    public static void main(String[] args) {
        try {
            AgentInterface agent = (AgentInterface) Naming.lookup("rmi://localhost/Agent");
            String name = agent.getName();
            System.out.println("Current name: " + name);
            agent.setName("New name");
            System.out.println("Name updated to: " + agent.getName());

            System.out.println("Current address: " + agent.getAddress());
            agent.setAddress("192.168.1.2");
            System.out.println("Address updated to: " + agent.getAddress());
        } catch (Exception e) {
            System.err.println("Manager exception: " + e);
            e.printStackTrace();
        }
    }
}