package ru.vsultanyarov.luxoftreportgenerator.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;

import java.util.List;

@Mapper
public interface OvertimeDao {
    @Update(value =
            "UPDATE overtimes " +
                    "SET overtime = #{jiraIssue.overtime} " +
                    "WHERE task_number=#{jiraIssue.taskNumber} " +
                    "AND username=#{username} " +
                    "AND is_weekend=#{jiraIssue.isWeekend}")
    void updateUserOvertime(JiraIssue jiraIssue, String username);

    @Insert(value =
            "INSERT INTO overtimes (username, task_number, overtime, is_weekend) " +
                    "VALUES (#{username}, #{jiraIssue.taskNumber}, #{jiraIssue.overtime}, #{jiraIssue.isWeekend})")
    void saveUserOvertime(JiraIssue jiraIssue, String username);

    JiraIssue findUserOvertimeByTaskNumberAndIsWeekend(String username, String taskNumber, Boolean isWeekend);

    List<JiraIssue> findAllUsersOvertimes(String username);

    @Delete(value = "DELETE FROM overtimes WHERE username=#{username}")
    void deleteOverworks(String username);
}
