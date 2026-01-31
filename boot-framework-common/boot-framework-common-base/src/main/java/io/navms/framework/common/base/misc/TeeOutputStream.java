package io.navms.framework.common.base.misc;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TeeOutputStream
 *
 * @author navms
 */
public class TeeOutputStream extends ProxyOutputStream {

    protected OutputStream branch;

    public TeeOutputStream(OutputStream out, OutputStream branch) {
        super(out);
        this.branch = branch;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.branch.close();
        }
    }

    public void flush() throws IOException {
        super.flush();
        this.branch.flush();
    }

    public synchronized void write(byte[] b) throws IOException {
        super.write(b);
        this.branch.write(b);
    }

    public synchronized void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        this.branch.write(b, off, len);
    }

    public synchronized void write(int b) throws IOException {
        super.write(b);
        this.branch.write(b);
    }

}
