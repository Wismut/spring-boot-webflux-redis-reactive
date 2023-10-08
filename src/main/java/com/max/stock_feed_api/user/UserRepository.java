package com.max.stock_feed_api.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

@Mapper
public interface UserRepository {
    //    @Select("SELECT * FROM users WHERE username=#{username}")
    Mono<User> findByUsername(@NonNull String username);

    //    @Update("UPDATE users SET api_key=#{apiKey} WHERE id=#{id}")
    void save(@NonNull User user);
}
