package org.openstack4j.model.common;

import java.io.Closeable;
import java.io.InputStream;

/**
 * Payload holds a reference to a Payload Object whether it be a file, inputstream or other and provides an input stream when uploading data to OpenStack.
 *
 * @param <T> the type of payload object
 * @author Jeremy Unruh
 */
public interface Payload<T> extends Closeable {

    /**
     * Opens and returns the input stream.
     *
     * @return the input stream
     */
    InputStream open();

    /**
     * Closes the stream and releases state.
     */
    void closeQuietly();

    /**
     * Gets the raw underlying object for the Payload.
     *
     * @return the raw object
     */
    T getRaw();

}
