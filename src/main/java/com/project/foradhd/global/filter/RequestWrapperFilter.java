package com.project.foradhd.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Order //필터 순서 마지막
@Component
public class RequestWrapperFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //Request 객체의 InputStream을 여러번 읽을 수 있도록 wrapping (logging을 위해)
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(requestWrapper, response);
    }
}
