package com.max.stock_feed_api.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.lang.NonNull;

import java.util.List;

@Mapper
public interface UserRepository {
    @Select("SELECT * FROM users")
    List<User> findAll();

    @Insert("INSERT INTO users(username, password, role) VALUES(#{username}, #{password}, #{role})")
    int save(@NonNull User user);

    @Select("SELECT * FROM users WHERE username=#{username}")
    User findByUsername(@NonNull String username);

    @Update("UPDATE users SET api_key=#{apiKey} WHERE id=#{id}")
    void update(@NonNull User user);
}
