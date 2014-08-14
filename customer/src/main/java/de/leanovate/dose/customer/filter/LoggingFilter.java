package de.leanovate.dose.customer.filter;

import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.UUID;

public class LoggingFilter implements Filter {
    public final static String SESSION_CORRELATION_ID = "X-Session-CorrelationId";

    public final static String REQUEST_CORRELATION_ID = "X-Request-CorrelationId";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("Bad servlet request");
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String sessionCorrelationId = httpServletRequest.getHeader(SESSION_CORRELATION_ID);
        String requestCorrelationId = httpServletRequest.getHeader(REQUEST_CORRELATION_ID);

        if (requestCorrelationId == null) {
            requestCorrelationId = UUID.randomUUID().toString();
        }

        if (sessionCorrelationId != null) {
            MDC.put(SESSION_CORRELATION_ID, sessionCorrelationId);
        }
        MDC.put(REQUEST_CORRELATION_ID, requestCorrelationId);

        chain.doFilter(request, response);

        MDC.clear();
    }

    @Override
    public void destroy() {

    }
}
