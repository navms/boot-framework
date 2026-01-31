package io.navms.framework.web.filter;

import io.navms.framework.web.servlet.RepeatableHttpServletRequestWrapper;
import io.navms.framework.web.servlet.RepeatableHttpServletResponseWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Http 过滤器
 *
 * @author navms
 */
public class HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        RepeatableHttpServletRequestWrapper requestWrapper;
        if (servletRequest instanceof RepeatableHttpServletRequestWrapper) {
            requestWrapper = (RepeatableHttpServletRequestWrapper) servletRequest;
        } else {
            requestWrapper = new RepeatableHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }

        RepeatableHttpServletResponseWrapper responseWrapper;
        if (servletResponse instanceof RepeatableHttpServletResponseWrapper) {
            responseWrapper = (RepeatableHttpServletResponseWrapper) servletResponse;
        } else {
            responseWrapper = new RepeatableHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        }

        filterChain.doFilter(requestWrapper, responseWrapper);
    }

}
