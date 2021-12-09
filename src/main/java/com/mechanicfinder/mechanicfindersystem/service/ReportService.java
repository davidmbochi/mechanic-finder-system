package com.mechanicfinder.mechanicfindersystem.service;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {
    String generatePendingApplications(LocalDate date, String fileFormat) throws IOException, JRException;
}
