package de.leanovate.dose.billing.logging;

import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

public class LoggingFilter implements Filter {
    public final static String CORRELATION_ID = "X-CorrelationId";

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

        String correlationId = httpServletRequest.getHeader(CORRELATION_ID);

        if (correlationId != null) {
            MDC.put(CORRELATION_ID, correlationId);
        }

        chain.doFilter(request, response);

        MDC.clear();
    }

    @Override
    public void destroy() {

    }
}
