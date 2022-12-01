package org.openstack4j.model.common.payloads;

import org.openstack4j.model.common.Payload;

import java.io.IOException;
import java.io.InputStream;

/**
 * Input Stream Payload
 *
 * @author Jeremy Unruh
 */
public class InputStreamPayload implements Payload<InputStream> {

    private InputStream is;

    public InputStreamPayload(InputStream is) {
        this.is = is;
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
    public InputStream open() {
        return is;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getRaw() {
        return is;
    }

}
