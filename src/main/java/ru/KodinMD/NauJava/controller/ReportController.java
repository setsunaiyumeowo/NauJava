package ru.KodinMD.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import ru.KodinMD.NauJava.entity.Report;
import ru.KodinMD.NauJava.entity.ReportStatus;
import ru.KodinMD.NauJava.service.ReportService;

@RestController
@RequestMapping("/custom/reports")
public class ReportController {
    private static final MediaType HTML_UTF8 = MediaType.parseMediaType("text/html;charset=UTF-8");
    private final ReportService reportService;
    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }
    @PostMapping
    public Long createReport(){
        Long id = reportService.createReport();
        reportService.generateReport(id);
        return id;
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getReport(@PathVariable Long id){
        Report report = reportService.getReport(id);
        if (report.getStatus() == ReportStatus.CREATED){
            return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(HTML_UTF8)
                    .body("<p>Отчет №" + id + " еще формируется, обновите страницу позже</p>");
        }
        if (report.getStatus() == ReportStatus.ERROR){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(HTML_UTF8)
                    .body("<p>При формировании отчета №" + id + " произошла ошибка: "
                            + HtmlUtils.htmlEscape(String.valueOf(report.getContent())) + "</p>");
        }
        return ResponseEntity.ok().contentType(HTML_UTF8).body(report.getContent());
    }
}
