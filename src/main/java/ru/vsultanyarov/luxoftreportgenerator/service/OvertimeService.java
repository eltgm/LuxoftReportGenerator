package ru.vsultanyarov.luxoftreportgenerator.service;

import org.springframework.stereotype.Service;
import ru.vsultanyarov.luxoftreportgenerator.dao.OvertimeDao;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;
import ru.vsultanyarov.luxoftreportgenerator.domain.Overwork;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.util.function.Predicate.not;


@Service
public class OvertimeService {
    private final OvertimeDao overtimeDao;

    public OvertimeService(OvertimeDao overtimeDao) {
        this.overtimeDao = overtimeDao;
    }

    public void updateOvertimes(JiraIssue jiraIssue, String username) {
        JiraIssue existedUserOvertime = overtimeDao.findUserOvertimeByTaskNumberAndIsWeekend(username, jiraIssue.getTaskNumber(), jiraIssue.getIsWeekend());

        if (existedUserOvertime != null) {
            jiraIssue.setOvertime(jiraIssue.getOvertime() + existedUserOvertime.getOvertime());
            overtimeDao.updateUserOvertime(jiraIssue, username);

            return;
        }

        overtimeDao.saveUserOvertime(jiraIssue, username);
    }

    public Overwork getOverwork(String username) {
        List<JiraIssue> allUsersOvertimes = overtimeDao.findAllUsersOvertimes(username);

        double weekendOvertime = allUsersOvertimes.stream()
                .filter(JiraIssue::getIsWeekend)
                .mapToDouble(JiraIssue::getOvertime)
                .reduce(0, Double::sum);
        double weekdayOvertime = allUsersOvertimes.stream()
                .filter(not(JiraIssue::getIsWeekend))
                .mapToDouble(JiraIssue::getOvertime)
                .reduce(0, Double::sum);
        String tasks = join(", ", allUsersOvertimes.stream()
                .map(JiraIssue::getTaskNumber)
                .collect(Collectors.toSet()));

        return new Overwork(weekendOvertime, weekdayOvertime, tasks);
    }

    public void deleteOverworks(String username) {
        overtimeDao.deleteOverworks(username);
    }
}
