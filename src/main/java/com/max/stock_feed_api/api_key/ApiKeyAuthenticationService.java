package com.max.stock_feed_api.api_key;

import com.max.stock_feed_api.user.User;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.apache.logging.log4j.util.Strings.isEmpty;

public class ApiKeyAuthenticationService {
    public static Authentication getAuthentication(@NonNull User user, @NonNull String requestApiKeyHeader) {
        if (isEmpty(requestApiKeyHeader) || isEmpty(user.getApiKey()) || !requestApiKeyHeader.equals(user.getApiKey())) {
            throw new BadCredentialsException("Invalid API Key");
        }
        return new ApiKeyAuthentication(requestApiKeyHeader, AuthorityUtils.NO_AUTHORITIES);
    }
}
