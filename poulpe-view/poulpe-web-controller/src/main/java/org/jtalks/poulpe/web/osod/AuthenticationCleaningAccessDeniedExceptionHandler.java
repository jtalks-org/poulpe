package org.jtalks.poulpe.web.osod;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class prevents application from going into dead loop. Becaus basic flow is:
 * 1). if user authenticated, then check authorization
 * 2). If authorization denied, then handle it with AccessDeniedHandlerImpl by default
 * 3). if user authenticated, then check authorization
 * 4). If authorization denied, then handle it with AccessDeniedHandlerImpl by default
 * ...
 * So this class removes authentication state, so this request will be considered as anonymous and no more authorization attempts will be made before redirect
 *
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
