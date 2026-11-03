package ru.KodinMD.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.entity.Report;
import ru.KodinMD.NauJava.entity.ReportStatus;
import ru.KodinMD.NauJava.exception.ResourceNotFoundException;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import ru.KodinMD.NauJava.repo.GeneratedPasswordRepository;
import ru.KodinMD.NauJava.repo.ReportRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportServiceImpl implements ReportService{
    private final ReportRepository reportRepository;
    private final AppUserRepository userRepository;
    private final GeneratedPasswordRepository passwordRepository;
    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             AppUserRepository userRepository,
                             GeneratedPasswordRepository passwordRepository){
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }
    @Override
    public Long createReport(){
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        return reportRepository.save(report).getId();
    }
    @Override
    public Report getReport(Long id){
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
    }
    @Override
    public CompletableFuture<Void> generateReport(Long id){
        return CompletableFuture.runAsync(() -> {
            Report report = getReport(id);
            try {
                long startTime = System.currentTimeMillis();
                AtomicLong usersCount = new AtomicLong();
                AtomicLong usersTime = new AtomicLong();
                AtomicReference<List<GeneratedPassword>> passwords = new AtomicReference<>();
                AtomicLong passwordsTime = new AtomicLong();
                AtomicReference<Exception> error = new AtomicReference<>();
                Thread usersThread = new Thread(() -> {
                    try {
                        long start = System.currentTimeMillis();
                        usersCount.set(userRepository.count());
                        usersTime.set(System.currentTimeMillis() - start);
                    } catch (Exception e){
                        error.set(e);
                    }
                });
                Thread passwordsThread = new Thread(() -> {
                    try {
                        long start = System.currentTimeMillis();
                        List<GeneratedPassword> list = new ArrayList<>();
                        passwordRepository.findAll().forEach(list::add);
                        passwords.set(list);
                        passwordsTime.set(System.currentTimeMillis() - start);
                    } catch (Exception e){
                        error.set(e);
                    }
                });
                usersThread.start();
                passwordsThread.start();
                usersThread.join();
                passwordsThread.join();
                if (error.get() != null){
                    throw error.get();
                }
                long totalTime = System.currentTimeMillis() - startTime;
                report.setContent(buildHtml(id, usersCount.get(), usersTime.get(),
                        passwords.get(), passwordsTime.get(), totalTime));
                report.setStatus(ReportStatus.COMPLETED);
            } catch (Exception e){
                if (e instanceof InterruptedException){
                    Thread.currentThread().interrupt();
                }
                report.setContent("Report generation failed: " + e.getMessage());
                report.setStatus(ReportStatus.ERROR);
            }
            reportRepository.save(report);
        });
    }
    private String buildHtml(Long id, long usersCount, long usersTime,
                             List<GeneratedPassword> passwords, long passwordsTime, long totalTime){
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Отчет №").append(id).append("</title></head><body>");
        html.append("<h1>Отчет №").append(id).append("</h1>");
        html.append("<h2>Статистика</h2><table border=\"1\">");
        html.append("<tr><th>Показатель</th><th>Значение</th></tr>");
        html.append("<tr><td>Количество зарегистрированных пользователей</td><td>").append(usersCount).append("</td></tr>");
        html.append("<tr><td>Время подсчета пользователей, мс</td><td>").append(usersTime).append("</td></tr>");
        html.append("<tr><td>Время получения списка паролей, мс</td><td>").append(passwordsTime).append("</td></tr>");
        html.append("<tr><td>Общее время формирования отчета, мс</td><td>").append(totalTime).append("</td></tr>");
        html.append("</table>");
        html.append("<h2>Сгенерированные пароли</h2><table border=\"1\">");
        html.append("<tr><th>ID</th><th>Label</th><th>Length</th><th>User</th><th>Algorithm</th><th>Category</th><th>Created</th></tr>");
        for (GeneratedPassword p : passwords){
            html.append("<tr><td>").append(p.getId())
                    .append("</td><td>").append(HtmlUtils.htmlEscape(p.getLabel()))
                    .append("</td><td>").append(p.getLength())
                    .append("</td><td>").append(HtmlUtils.htmlEscape(p.getUser().getLogin()))
                    .append("</td><td>").append(HtmlUtils.htmlEscape(p.getAlgorithm().getName()))
                    .append("</td><td>").append(p.getCategory() == null ? "-" : HtmlUtils.htmlEscape(p.getCategory().getTitle()))
                    .append("</td><td>").append(p.getCreatedAt())
                    .append("</td></tr>");
        }
        html.append("</table></body></html>");
        return html.toString();
    }
}
