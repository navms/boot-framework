package io.navms.framework.web.servlet;

import io.navms.framework.common.base.misc.TeeOutputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 解决重复读取响应流的问题
 *
 * @author navms
 */
public class RepeatableHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream filterOutput;
    private final ByteArrayOutputStream output;

    public RepeatableHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (filterOutput == null) {
            filterOutput = new ServletOutputStream() {

                private final TeeOutputStream teeOutputStream = new TeeOutputStream(
                        RepeatableHttpServletResponseWrapper.super.getOutputStream(), output);

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                }

                @Override
                public void write(int b) throws IOException {
                    teeOutputStream.write(b);
                }

            };
        }
        return filterOutput;
    }

    public byte[] toByteArray() {
        return output.toByteArray();
    }

}
