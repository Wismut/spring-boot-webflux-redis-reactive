package com.max.stock_feed_api.jwt;

import com.max.stock_feed_api.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var user = userRepository.findByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
