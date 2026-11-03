package ru.KodinMD.NauJava.service;

import ru.KodinMD.NauJava.entity.Report;
import java.util.concurrent.CompletableFuture;
public interface ReportService {
    Long createReport();
    Report getReport(Long id);
    CompletableFuture<Void> generateReport(Long id);
}
