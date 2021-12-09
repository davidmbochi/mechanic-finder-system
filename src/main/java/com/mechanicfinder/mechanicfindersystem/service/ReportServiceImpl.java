package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final MechanicRepository mechanicRepository;
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    @Override
    public String generatePendingApplications(LocalDate date, String fileFormat) throws IOException, JRException {
        List<Mechanic> mechanics = mechanicRepository.findAllByCreatedAt(date);
        //load file and compile it
        String resourceLocation = "classpath:pending.jrxml";
        JasperPrint jasperPrint = getJasperPrint(mechanics, resourceLocation);
        //create a folder to store the report
        String fileName = "/"+"pending.pdf";
        Path uploadPath = getUploadPath(fileFormat, jasperPrint, fileName);
        //create a private method that returns the link to the specific file
        return getPdfFileLink(uploadPath);

    }

    private JasperPrint getJasperPrint(List<Mechanic> pendingApplications, String resourceLocation) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile(resourceLocation);
        JasperReport jasperReport = JasperCompileManager
                .compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource =  new
                JRBeanCollectionDataSource(pendingApplications);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy","Mechanic Finder");

        JasperPrint jasperPrint = JasperFillManager
                .fillReport(jasperReport,parameters,dataSource);
        return jasperPrint;
    }

    private Path getUploadPath(String fileFormat, JasperPrint jasperPrint, String fileName) throws IOException, JRException {
        String uploadDir = StringUtils.cleanPath("./generated-reports");
        Path uploadPath = Paths.get(uploadDir);
        logger.info("pdf path: ===================>"+uploadPath.toAbsolutePath());
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        //generate the report and save it the created folder
        if (fileFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile
                    (jasperPrint,uploadPath+fileName);
        }
        return uploadPath;
    }

    private String getPdfFileLink(Path uploadPath){
        return uploadPath+"/"+"pending.pdf";
    }
}
