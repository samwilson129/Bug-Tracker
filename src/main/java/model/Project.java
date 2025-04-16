package main.java.model;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Project {
    private int id;
    private String name;
    private String description;
    private List<String> bugs;

    private int managerId; // Foreign key to Users table
    private List<Integer> developerIds; // List of developer IDs associated with the project

    public Project() {
    }

    public Project(int id, String name, String description, List<String> bugs, int managerId,
            List<Integer> developerIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bugs = bugs;
        this.managerId = managerId;
        this.developerIds = developerIds;
    }

    public int getter_id() {
        return id;
    }

    public String getter_name() {
        return name;
    }

    public String getter_description() {
        return description;
    }

    public List<String> getter_bugs() {
        System.out.println("Bugs: " + bugs);
        return bugs;
    }

    // public String getBugsAsJsonString() {
    // try {
    // ObjectMapper objectMapper = new ObjectMapper();
    // System.out.println("Bug in getBugsAsJsonString:
    // "+objectMapper.writeValueAsString(bugs)); // bugs is the List<String>
    // return objectMapper.writeValueAsString(bugs); // bugs is the List<String>
    // } catch (Exception e) {
    // System.out.println("Failed to convert bugs to JSON string: " +
    // e.getMessage());
    // e.printStackTrace();
    // return "[]"; // fallback to empty array
    // }
    // }

    public String getBugsAsJsonString() {
        try {
            System.out.println("Bugs before reconstructing: " + bugs);
            String bugsString = bugs.toString();
            System.out.println("Bugs after converting to string: " + bugsString.substring(1, bugsString.length() - 1));
            return bugsString.substring(1, bugsString.length() - 1);
            // String reconstructedJson = String.join("", bugs);
            // System.out.println("Reconstructed JSON string: " + reconstructedJson);
            // return reconstructedJson;
        } catch (Exception e) {
            System.out.println("Failed to convert bugs to JSON string: " + e.getMessage());
            return "[]";
        }
    }

    public int getter_managerId() {
        return managerId;
    }

    public List<Integer> getter_developerIds() {
        return developerIds;
    }

    public void setter_id(int id) {
        this.id = id;
    }

    public void setter_name(String name) {
        this.name = name;
    }

    public void setter_description(String description) {
        this.description = description;
    }

    public void setter_bugs(List<String> bugs) {
        this.bugs = bugs;
    }

    public void setter_managerId(int managerId) {
        this.managerId = managerId;
    }

    public void setter_developerIds(List<Integer> developerIds) {
        this.developerIds = developerIds;
    }
}
