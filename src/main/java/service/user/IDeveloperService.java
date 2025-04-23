package main.java.service.user;

import main.java.model.Bug;
import java.util.List;

/**
 * Developer-specific service interface
 * Implements Interface Segregation Principle - clients only depend on methods they use
 */
public interface IDeveloperService extends IUserService {
    boolean fixBug(int bugId, String developer);
    Bug viewBug(int bugId);
    List<Bug> getBugsAssignedTo(String developerEmail);
}