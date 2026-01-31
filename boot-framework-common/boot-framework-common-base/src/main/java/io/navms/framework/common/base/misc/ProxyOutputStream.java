package io.navms.framework.common.base.misc;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ProxyOutputStream
 *
 * @author navms
 */
public class ProxyOutputStream extends FilterOutputStream {

    public ProxyOutputStream(OutputStream proxy) {
        super(proxy);
    }

    protected void afterWrite(int n) throws IOException {
    }

    protected void beforeWrite(int n) throws IOException {
    }

    public void close() throws IOException {
        if (this.out != null) {
            try {
                this.out.close();
            } catch (IOException e) {
                this.handleIOException(e);
            }
        }
    }

    public void flush() throws IOException {
        try {
            this.out.flush();
        } catch (IOException e) {
            this.handleIOException(e);
        }
    }

    protected void handleIOException(IOException e) throws IOException {
        throw e;
    }

    public void write(byte[] bts) throws IOException {
        try {
            int len = bts == null ? 0 : bts.length;
            this.beforeWrite(len);
            this.out.write(bts);
            this.afterWrite(len);
        } catch (IOException e) {
            this.handleIOException(e);
        }

    }

    public void write(byte[] bts, int st, int end) throws IOException {
        try {
            this.beforeWrite(end);
            this.out.write(bts, st, end);
            this.afterWrite(end);
        } catch (IOException e) {
            this.handleIOException(e);
        }

    }

    public void write(int idx) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.write(idx);
            this.afterWrite(1);
        } catch (IOException e) {
            this.handleIOException(e);
        }
    }

}
