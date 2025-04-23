package main.java.service.user;

import main.java.model.Project;
import java.util.List;

/**
 * ProjectManager-specific service interface
 * Implements Interface Segregation Principle - clients only depend on methods they use
 */
public interface IProjectManagerService extends IUserService {
    boolean assignBug(int bugId, String developer);
    boolean createProject(String name, String description, int managerId);
    List<Project> getProjectsForManager(int managerId);
}