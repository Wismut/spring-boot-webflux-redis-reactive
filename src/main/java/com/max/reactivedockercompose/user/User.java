package com.max.reactivedockercompose.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private String token;
    private Role role;
}
