package org.jtalks.common.security.web;


import org.jtalks.common.model.entity.User;
import org.jtalks.common.security.user.LastLoginTimeServiceMixin;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that activated when user successfully authenticated.
 *
 * @author Kirill Afonin
 */
public class SuccessfulAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private LastLoginTimeServiceMixin lastLoginTimeService;

    /**
     * Constructor.
     *
     * @param lastLoginTimeService service for users related actions
     */
    public SuccessfulAuthenticationHandler(LastLoginTimeServiceMixin lastLoginTimeService) {
        this.lastLoginTimeService = lastLoginTimeService;
    }

    /**
     * Handle user's successfull authentication.
     * Updates last login time for authenticated user.
     *
     * @param request        http request
     * @param response       http response
     * @param authentication user's authentication
     * @throws javax.servlet.ServletException .
     * @throws java.io.IOException            .
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        lastLoginTimeService.updateLastLoginTime(user);
        logger.info("User logged in: " + user.getUsername());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
