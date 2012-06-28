package org.jtalks.poulpe.web.osod;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dionis
 *         6/28/12 10:01 PM
 */
public class AuthenticationCleaningAccessDeniedExceptionHandler extends AccessDeniedHandlerImpl {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(null);
        super.handle(request, response, accessDeniedException);
    }
}
