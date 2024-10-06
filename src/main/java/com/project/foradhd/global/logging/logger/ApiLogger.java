package com.project.foradhd.global.logging.logger;

import com.project.foradhd.global.util.HeaderUtil;
import com.project.foradhd.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class ApiLogger {

    private static final String REQUEST_LOG_FORMAT = """
            [REQUEST]
            API : {} {}
            Token : {}
            ClientIp : {}
            Body : {}
            """;
    private static final String RESPONSE_LOG_FORMAT = """
            [RESPONSE]
            Status : {},
            Body : {}
            """;
    private final HeaderUtil headerUtil;

    //-Controller 메소드 호출 시 동작하는 포인트컷 표현식
    @Around("""
        execution(public * com.project.foradhd..*Controller.*(..))
    """)
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        loggingRequest();
        Object result = joinPoint.proceed();
        loggingResponse(result);
        return result;
    }

    private void loggingRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes servletRequestAttributes &&
                servletRequestAttributes.getRequest() instanceof ContentCachingRequestWrapper request) {
            log.info(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestURI(), headerUtil.parseToken(request).orElse(""),
                    request.getRemoteAddr(), request.getContentAsString());
        }
    }

    private void loggingResponse(Object result) {
        if (result instanceof ResponseEntity<?> response)
            log.info(RESPONSE_LOG_FORMAT, response.getStatusCode(), JsonUtil.writeValueAsString(response.getBody()));
        else {
            log.info(RESPONSE_LOG_FORMAT, HttpStatus.OK, result);
        }
    }
}
