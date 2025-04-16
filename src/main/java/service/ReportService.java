package main.java.service;

import java.util.List;

import main.java.DAO.ReportDAO;
import main.java.model.Report;

public class ReportService {
    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    // Add a new report
    public boolean addReport(Report report) {
        try {
            reportDAO.addReport(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a report by ID
    public Report getReportById(int id) {
        return reportDAO.getReportById(id);
    }

    // Get all reports
    public List<Report> getAllReports() {
        return reportDAO.getAllReports();
    }

    // Delete a report by ID
    public boolean deleteReport(int id) {
        return reportDAO.deleteReport(id);
    }

}