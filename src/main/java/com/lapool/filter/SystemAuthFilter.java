/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.filter;

/**
 *
 * @author akshat666
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.lapool.bean.SessionBean;
import java.io.IOException;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dell
 */
public class SystemAuthFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SystemAuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String methodName = "doFilter()";
        log.info("Entering :" + methodName);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if (session == null) 
        {
            log.info("Session is NULL");
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/pages/troofy/secure/login.xhtml");
        }
        else 
        {
            SessionBean bean = (SessionBean) session.getAttribute("sessionBean");

            if (bean == null) 
            {
                ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/pages/troofy/secure/login.xhtml");
                log.info("Exiting due to NULL session:" + methodName);
            }
            else if(bean.getSystemUser() == null)
            {
                ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/pages/troofy/secure/login.xhtml");
                log.info("User not logged in" + methodName);
            }
            else 
            {
                if (!httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
                    httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    httpResponse.setDateHeader("Expires", 0); // Proxies.
                }
                log.info("Exiting :" + methodName);
                chain.doFilter(request, response); // Logged in, so just continue.
            }
        }





    }

    @Override
    public void destroy() {
    }
}
