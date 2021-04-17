package ru.vsultanyarov.luxoftreportgenerator.service;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vsultanyarov.luxoftreportgenerator.dao.UserDao;
import ru.vsultanyarov.luxoftreportgenerator.domain.ReportDataInfo;
import ru.vsultanyarov.luxoftreportgenerator.domain.User;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Service
public class WorkCalendarService {
    private static final String WORK_DAYS_DAYS_OFF_URL = "https://isdayoff.ru/api/getdata?year=%d&month=%d&cc=ru";

    private final RestTemplate restTemplate;
    private final UserDao userDao;

    public WorkCalendarService(RestTemplate restTemplate,
                               UserDao userDao) {
        this.restTemplate = restTemplate;
        this.userDao = userDao;
    }

    public ReportDataInfo getReportData(String userName) {
        LocalDate now = LocalDate.now();

        ReportDataInfo workDaysAndDayOffsInfo = getWorkDaysAndDayOffsInfo(now);
        User user = userDao.getUser(userName);
        String month = new SimpleDateFormat("LLLL", new Locale("ru", "RU")).format(now.toDate());

        workDaysAndDayOffsInfo.setMissedDays(user.getMissedDays());
        workDaysAndDayOffsInfo.setReportGenerateMonth(month);
        return workDaysAndDayOffsInfo;
    }

    public void updateMissedDays(User user) {
        userDao.updateUserMissedDays(user);
    }

    public ReportDataInfo getWorkDaysAndDayOffsInfo(LocalDate now) {
        int month = now.getMonthOfYear();
        int year = now.getYear();

        var workDaysAndDaysOff = "";
        workDaysAndDaysOff = restTemplate.getForObject(
                String.format(WORK_DAYS_DAYS_OFF_URL, year, month), String.class);

        var workDays = 0;
        var daysOff = 0;
        var reportGenerateDay = 0;

        for (var i = 0; i < workDaysAndDaysOff.length(); i++) {
            if (workDaysAndDaysOff.charAt(i) == '1') {
                daysOff++;
            } else {
                workDays++;
                reportGenerateDay = i + 1;
            }
        }

        return ReportDataInfo.builder()
                .daysOff(daysOff)
                .workDays(workDays)
                .reportGenerateDay(reportGenerateDay)
                .build();
    }
}