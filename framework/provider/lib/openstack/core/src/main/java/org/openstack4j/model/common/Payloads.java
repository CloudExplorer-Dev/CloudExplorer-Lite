package org.openstack4j.model.common;

import org.openstack4j.model.common.payloads.FilePayload;
import org.openstack4j.model.common.payloads.InputStreamPayload;
import org.openstack4j.model.common.payloads.URLPayload;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class for creating supported Payloads.
 *
 * @author Jeremy Unruh
 */
public class Payloads {

    /**
     * Creates a new File based Payload
     *
     * @param file the file to send
     * @return the Payload
     */
    public static Payload<File> create(File file) {
        return new FilePayload(file);
    }

    /**
     * Creates a new Input Stream based Payload
     *
     * @param stream the input stream
     * @return the Payload
     */
    public static Payload<InputStream> create(InputStream stream) {
        return new InputStreamPayload(stream);
    }

    /**
     * Creates a new URL based Payload allowing direct upload from the URL
     *
     * @param url the URL
     * @return the Payload
     */
    public static Payload<URL> create(URL url) {
        return new URLPayload(url);
    }

}
