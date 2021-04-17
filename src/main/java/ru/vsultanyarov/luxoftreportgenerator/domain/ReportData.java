package ru.vsultanyarov.luxoftreportgenerator.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportData {
    private String reportCreateDay;
    private String reportCreateMonth;
    private String workDays;
    private String reportCreateYear;
    private List<JiraIssue> issues;
}