package ru.vsultanyarov.luxoftreportgenerator.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import ru.vsultanyarov.luxoftreportgenerator.domain.User;

@Mapper
public interface UserDao {
    User getUser(String username);

    void updateUserMissedDays(User user);

    @Update(value = "UPDATE users SET password = #{password} WHERE username='eltgm'")
    void setPassword(String password);
}