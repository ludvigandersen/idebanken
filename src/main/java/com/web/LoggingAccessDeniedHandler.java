package com.web;

import com.controller.FrontpageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Denne klasse bliver brugt til at vise HTML siden access-denied.html n&aring;r man ikke er logget ind
 * og requester en side som man skal vaelig;re logget ind med en bestemt user role for at se.
 * @author Mikkel
 */

@Component
public class LoggingAccessDeniedHandler implements AccessDeniedHandler {

    private static Logger log = LoggerFactory.getLogger(LoggingAccessDeniedHandler.class);

    /**
     * Denne metode tager sig af at redirecte brugeren til metoden {@link FrontpageController#accessDenied()}
     * hvis brugeren ikke er autoriseret til at se den efterspurgte side.
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            log.info(auth.getName()
                    + " was trying to access protected resource: "
                    + request.getRequestURI());
        }

        response.sendRedirect(request.getContextPath() + "/access-denied");

    }
}