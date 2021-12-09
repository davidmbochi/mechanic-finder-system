package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/pending")
    public String generatePendingApplications(@RequestParam("startTime")String date,
                                              @RequestParam("fileFormat")String fileFormat) throws JRException, IOException {
        LocalDate findDate = LocalDate.parse(date);
        String reportLink = reportService.generatePendingApplications(findDate, fileFormat);
        return "redirect:/"+reportLink;
    }
}
