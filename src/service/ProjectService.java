package service;

import DAO.ProjectDAO;
import model.Project;
import java.util.List;

public class ProjectService {
    private ProjectDAO projectDAO;

    public ProjectService() {
        this.projectDAO = new ProjectDAO();
    }

    // Add a new project
    public void addProject(Project project) {
        try {
            projectDAO.addProject(project);
            System.out.println("Project added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding project: " + e.getMessage());
        }
    }

    // Retrieve a project by its ID
    public Project getProjectById(int id) {
        try {
            Project project = projectDAO.getProjectById(id);
            if (project == null) {
                System.out.println("Project not found with ID: " + id);
            }
            return project;
        } catch (Exception e) {
            System.err.println("Error retrieving project: " + e.getMessage());
            return null;
        }
    }

    // Retrieve all projects
    public List<Project> getAllProjects() {
        try {
            return projectDAO.getAllProjects();
        } catch (Exception e) {
            System.err.println("Error retrieving projects: " + e.getMessage());
            return null;
        }
    }

    // Update an existing project
    public boolean updateProject(Project project) {
        try {
            boolean updated = projectDAO.updateProject(project);
            if (updated) {
                System.out.println("Project updated successfully.");
            } else {
                System.out.println("Project update failed.");
            }
            return updated;
        } catch (Exception e) {
            System.err.println("Error updating project: " + e.getMessage());
            return false;
        }
    }

    // Delete a project by its ID
    public boolean deleteProject(int id) {
        try {
            boolean deleted = projectDAO.deleteProject(id);
            if (deleted) {
                System.out.println("Project deleted successfully.");
            } else {
                System.out.println("Project deletion failed.");
            }
            return deleted;
        } catch (Exception e) {
            System.err.println("Error deleting project: " + e.getMessage());
            return false;
        }
    }
}
