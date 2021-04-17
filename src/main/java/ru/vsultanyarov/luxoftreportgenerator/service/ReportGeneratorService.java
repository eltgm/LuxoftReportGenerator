package ru.vsultanyarov.luxoftreportgenerator.service;

import com.aspose.words.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.vsultanyarov.luxoftreportgenerator.config.ReportProperties;
import ru.vsultanyarov.luxoftreportgenerator.dao.UserDao;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;
import ru.vsultanyarov.luxoftreportgenerator.domain.ReportData;
import ru.vsultanyarov.luxoftreportgenerator.domain.ReportDataInfo;
import ru.vsultanyarov.luxoftreportgenerator.domain.User;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static org.joda.time.LocalDate.now;
import static org.springframework.util.StringUtils.hasText;

@Service
public class ReportGeneratorService {
    private static final String FONT_NAME = "Times New Roman";

    private final UserDao userDao;
    private final WorkCalendarService workCalendarService;
    private final ReportProperties reportProperties;
    private final EmailService emailService;
    private final JiraService jiraService;

    public ReportGeneratorService(UserDao userDao,
                                  WorkCalendarService workCalendarService,
                                  ReportProperties reportProperties,
                                  EmailService emailService,
                                  JiraService jiraService) {
        this.jiraService = jiraService;
        this.userDao = userDao;
        this.workCalendarService = workCalendarService;
        this.reportProperties = reportProperties;
        this.emailService = emailService;
    }

    public void startReportGeneration() {
        ReportDataInfo workDaysAndDayOffsInfo = workCalendarService.getWorkDaysAndDayOffsInfo(now());
        Calendar calendar = getInstance();

        String reportCreateMonth = calendar.getDisplayName(Calendar.MONTH,
                Calendar.LONG_FORMAT, new Locale("ru"));
        String reportCreateDay = String.valueOf(workDaysAndDayOffsInfo.getReportGenerateDay());
        String reportCreateYear = String.valueOf(calendar.get(YEAR));

        User user = userDao.getUser(reportProperties.getUsername());
        String workDays = String.valueOf(workDaysAndDayOffsInfo.getWorkDays() - user.getMissedDays());
        List<JiraIssue> issues = jiraService.getJiraIssues(new ArrayList<>(), "");
        issues.sort(Comparator.comparing(JiraIssue::getTaskNumber));

        ReportData reportData = ReportData.builder()
                .reportCreateDay(reportCreateDay)
                .reportCreateMonth(reportCreateMonth)
                .reportCreateYear(reportCreateYear)
                .issues(issues)
                .workDays(workDays)
                .build();

        String fileName = generateReport(reportData);
        if (hasText(fileName)) {
            emailService.sendReport(fileName);
        }
    }

    @SneakyThrows
    private String generateReport(ReportData reportData) {
        InputStream targetStream = getClass().getClassLoader()
                .getResourceAsStream(String.format("%s/%s", reportProperties.getTemplate().getPath(), reportProperties.getTemplate().getName()));
        var doc = new Document(targetStream);

        Table tasksTable = (Table) doc.getChild(NodeType.TABLE, 0, true);
        Table resultTable = (Table) doc.getChild(NodeType.TABLE, 1, true);
        for (JiraIssue issue : reportData.getIssues()) {
            fillTable(doc, tasksTable, issue, false);
            fillTable(doc, resultTable, issue, true);
        }

        var engine = new ReportingEngine();
        engine.buildReport(doc, reportData, "report");
        String fileName = String.format(reportProperties.getTemplate().getName(),
                new SimpleDateFormat("LLLL", new Locale("ru", "RU")).format(now().toDate()));
        SaveOutputParameters save = doc.save(fileName);

        return save == null ? null : fileName;
    }

    private void fillTable(Document doc, Table table, JiraIssue issue, boolean needStatus) {
        var tasksRow = new Row(doc);
        table.appendChild(tasksRow);

        var taskNumberCell = new Cell(doc);
        taskNumberCell.appendChild(new Paragraph(doc));
        taskNumberCell.getCellFormat().setWrapText(false);
        var taskNumberRun = new Run(doc, issue.getTaskNumber());
        taskNumberRun.getFont().setName(FONT_NAME);
        taskNumberCell.getFirstParagraph().appendChild(taskNumberRun);
        tasksRow.appendChild(taskNumberCell);

        var taskTitleCell = new Cell(doc);
        taskTitleCell.appendChild(new Paragraph(doc));
        var taskTitleRun = new Run(doc, issue.getTaskTitle());
        taskTitleRun.getFont().setName(FONT_NAME);
        taskTitleCell.getFirstParagraph().appendChild(taskTitleRun);
        tasksRow.appendChild(taskTitleCell);

        if (needStatus) {
            var taskStatusCell = new Cell(doc);
            taskStatusCell.appendChild(new Paragraph(doc));
            var taskStatusCellRun = new Run(doc, issue.getTaskStatus());
            taskStatusCellRun.getFont().setName(FONT_NAME);
            taskStatusCell.getFirstParagraph().appendChild(taskStatusCellRun);
            tasksRow.appendChild(taskStatusCell);
        }
    }
}