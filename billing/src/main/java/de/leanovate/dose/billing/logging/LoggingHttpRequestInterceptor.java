package de.leanovate.dose.billing.logging;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.MDC;

import java.io.IOException;

public class LoggingHttpRequestInterceptor implements HttpRequestInterceptor {
    @Override
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {

        final String correlationId = (String)MDC.get(LoggingFilter.CORRELATION_ID);

        if ( correlationId != null ) {
            request.addHeader(LoggingFilter.CORRELATION_ID, correlationId);
        }
    }
}
