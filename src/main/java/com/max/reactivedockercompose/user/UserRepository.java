package com.max.reactivedockercompose.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRepository {
    @Select("SELECT * FROM USERS")
    List<User> findAll();

    @Insert("INSERT INTO users(username, password, role) VALUES(#{username}, #{password}, #{role})")
    int save(User user);

    @Select("SELECT * FROM USERS WHERE username=#{username}")
    User findByUsername(String username);
}
