package net.az3l1t.aspect;

import net.az3l1t.config.properties.HttpLoggingProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public class HttpLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(HttpLoggingAspect.class);
    private final HttpLoggingProperties httpLoggingProperties;

    public HttpLoggingAspect(HttpLoggingProperties httpLoggingProperties) {
        this.httpLoggingProperties = httpLoggingProperties;
    }

    @Around("@annotation(net.az3l1t.annotation.LoggingHttp)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder logMessage = new StringBuilder();
        long executionTime;

        if (httpLoggingProperties.isRequest()) {
            String args = Arrays.toString(joinPoint.getArgs());
            logMessage.append(String.format("Method %s called with args: %s ",
                    joinPoint.getSignature().toShortString(), args));
        }

        Object result;
        if (httpLoggingProperties.isTime()) {
            long startTime = System.currentTimeMillis();
            result = joinPoint.proceed();
            executionTime = System.currentTimeMillis() - startTime;
            logMessage.append(String.format("Time taken: %s; ", executionTime));
        } else {
            result = joinPoint.proceed();
        }

        if (httpLoggingProperties.isResponse()) {
            logMessage.append(String.format("Response of request: %s; ", result));
        }

        if (!logMessage.isEmpty()) {
            log(logMessage.toString());
        }

        return result;
    }

    private void log(String message) {
        switch (httpLoggingProperties.getLevel()) {
            case DEBUG -> logger.debug(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
            default -> logger.info(message);
        }
    }
}
