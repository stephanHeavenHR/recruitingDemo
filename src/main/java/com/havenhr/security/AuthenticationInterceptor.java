package com.havenhr.security;

import com.havenhr.entity.User;
import com.havenhr.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    public static final String X_AUTH_TOKEN = "X-Auth-Token";

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String authToken = request.getHeader(X_AUTH_TOKEN);
        if (StringUtils.isEmpty(authToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token.");
            return false;
        }

        final User user = userRepository.findByToken(authToken);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found.");
            return false;
        }
        return true;
    }

}