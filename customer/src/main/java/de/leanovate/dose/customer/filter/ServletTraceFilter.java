package de.leanovate.dose.customer.filter;

import com.github.kristofa.brave.BraveHttpHeaders;
import com.github.kristofa.brave.ServerTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

public class ServletTraceFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(ServletTraceFilter.class);

    private final ServerTracer serverTracer;

    public ServletTraceFilter(final ServerTracer serverTracer) {

        this.serverTracer = serverTracer;
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        serverTracer.clearCurrentSpan();
        if (request instanceof HttpServletRequest) {
            TraceData traceData = getTraceDataFromHeaders(request);
            if (Boolean.FALSE.equals(traceData.shouldBeTraced())) {
                serverTracer.setStateNoTracing();
                logger.debug("Not tracing request");
            } else {
                String spanName = getSpanName(traceData, (HttpServletRequest) request);
                if (traceData.getTraceId() != null && traceData.getSpanId() != null) {
                    logger.debug("Received span information as part of request");
                    serverTracer.setStateCurrentTrace(traceData.getTraceId(), traceData.getSpanId(),
                            traceData.getParentSpanId(), spanName);
                } else {
                    logger.debug("Received no span state");
                    serverTracer.setStateUnknown(spanName);
                }
            }
        }
        serverTracer.setServerReceived();
        chain.doFilter(request, response);
        serverTracer.setServerSend();
    }

    @Override
    public void destroy() {

    }

    private String getSpanName(TraceData traceData, HttpServletRequest request) {

        if (traceData.getSpanName() == null || traceData.getSpanName().isEmpty()) {
            return request.getMethod() + " " + request.getRequestURI();
        }
        return traceData.getSpanName();
    }

    private TraceData getTraceDataFromHeaders(ServletRequest request) {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        TraceData traceData = new TraceData();
        traceData.setTraceId(longOrNull(httpRequest.getHeader(BraveHttpHeaders.TraceId.getName())));
        traceData.setSpanId(longOrNull(httpRequest.getHeader(BraveHttpHeaders.SpanId.getName())));
        traceData.setParentSpanId(longOrNull(httpRequest.getHeader(BraveHttpHeaders.ParentSpanId.getName())));
        traceData.setShouldBeSampled(nullOrBoolean(httpRequest.getHeader(BraveHttpHeaders.Sampled.getName())));
        traceData.setSpanName(httpRequest.getHeader(BraveHttpHeaders.SpanName.getName()));
        return traceData;
    }

    private Boolean nullOrBoolean(String value) {

        return (value == null) ? null : Boolean.valueOf(value);
    }

    private Long longOrNull(String value) {

        if (value == null) {
            return null;
        }
        return Long.parseLong(value, 16);
    }
}
