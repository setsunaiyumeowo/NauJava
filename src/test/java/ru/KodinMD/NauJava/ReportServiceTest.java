package ru.KodinMD.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.KodinMD.NauJava.entity.Report;
import ru.KodinMD.NauJava.entity.ReportStatus;
import ru.KodinMD.NauJava.exception.ResourceNotFoundException;
import ru.KodinMD.NauJava.service.ReportService;

@SpringBootTest
class ReportServiceTest {
    private final ReportService reportService;
    @Autowired
    ReportServiceTest(ReportService reportService){
        this.reportService = reportService;
    }
    @Test
    void testCreateReport(){
        Long id = reportService.createReport();
        Assertions.assertEquals(ReportStatus.CREATED, reportService.getReport(id).getStatus());
    }
    @Test
    void testGenerateReport(){
        Long id = reportService.createReport();
        reportService.generateReport(id).join();
        Report report = reportService.getReport(id);
        Assertions.assertEquals(ReportStatus.COMPLETED, report.getStatus());
        Assertions.assertTrue(report.getContent().contains("Количество зарегистрированных пользователей"));
        Assertions.assertTrue(report.getContent().contains("Общее время формирования отчета"));
    }
    @Test
    void testUnknownReport(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reportService.getReport(-1L));
    }
}
