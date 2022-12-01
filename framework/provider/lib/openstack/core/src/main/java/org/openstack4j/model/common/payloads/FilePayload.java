package org.openstack4j.model.common.payloads;

import com.google.common.base.Throwables;
import org.openstack4j.model.common.Payload;

import java.io.*;

/**
 * A File based Payload
 *
 * @author Jeremy Unruh
 */
public class FilePayload implements Payload<File> {

    private File file;
    private InputStream is;

    public FilePayload(File file) {
        this.file = file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream open() {
        try {
            is = new FileInputStream(file);
            return is;
        } catch (FileNotFoundException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getRaw() {
        return file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (is != null)
            is.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeQuietly() {
        try {
            close();
        } catch (IOException e) {
        }
    }

}
