package main.java.service.user;

import main.java.model.Bug;

/**
 * Tester-specific service interface
 * Implements Interface Segregation Principle - clients only depend on methods they use
 */
public interface ITesterService extends IUserService {
    void reportBug(String title, String description, String reportedBy, int projectId);
    boolean verifyBugFix(int bugId);
}