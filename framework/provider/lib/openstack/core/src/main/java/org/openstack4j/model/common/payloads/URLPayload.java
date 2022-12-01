package org.openstack4j.model.common.payloads;

import com.google.common.base.Throwables;
import org.openstack4j.model.common.Payload;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * URL based Payload
 *
 * @author Jeremy Unruh
 */
public class URLPayload implements Payload<URL> {

    URL url;
    InputStream is;

    public URLPayload(URL url) {
        this.url = url;
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
        try {
            is = url.openStream();
            return is;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeQuietly() {
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getRaw() {
        return url;
    }

}
