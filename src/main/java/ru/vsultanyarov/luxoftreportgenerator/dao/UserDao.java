package ru.vsultanyarov.luxoftreportgenerator.dao;


import org.apache.ibatis.annotations.Mapper;
import ru.vsultanyarov.luxoftreportgenerator.domain.User;

@Mapper
public interface UserDao {
    User findUserByUsername(String username);

    void updateUserMissedDays(User user);
}