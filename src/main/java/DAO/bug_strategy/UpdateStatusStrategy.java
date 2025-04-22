package main.java.DAO.bug_strategy;

import main.java.model.Bug;
import main.java.model.BugStatus;
import main.java.DAO.bug_repository.IBugRepository;

import java.time.LocalDateTime;


public class UpdateStatusStrategy implements BugOperationStrategy {
    private final IBugRepository bugRepository;
    private final BugStatus newStatus;
    
    public UpdateStatusStrategy(IBugRepository bugRepository, BugStatus newStatus) {
        this.bugRepository = bugRepository;
        this.newStatus = newStatus;
    }
    
    @Override
    public boolean execute(Bug bug) {
        bug.setter_bugstatus(newStatus);
        bug.setter_updatedAt(LocalDateTime.now());
        return bugRepository.updateBug(bug);
    }
}