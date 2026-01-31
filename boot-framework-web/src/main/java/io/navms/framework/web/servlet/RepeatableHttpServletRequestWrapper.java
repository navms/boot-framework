package io.navms.framework.web.servlet;

import io.navms.framework.common.base.utils.IOUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;

import java.io.*;
import java.util.Map;

/**
 * 解决重复读取请求流的问题
 *
 * @author navms
 */
public class RepeatableHttpServletRequestWrapper extends HttpServletRequestWrapper {

    @Getter
    private final byte[] cachedBytes;
    private final HttpServletRequest request;
    private final Map<String, String[]> parameterMap;

    public RepeatableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
        this.parameterMap = request.getParameterMap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), byteArrayOutputStream);
        this.cachedBytes = byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBytes);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

}
