package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

/**
 * A Personality is a file (path to file) and the contents that should be replaced on a new VM Server
 *
 * @author Jeremy Unruh
 */
public class Personality implements ModelEntity {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String path;
    private String contents;

    public Personality() {
    }

    public Personality(String path, String contents) {
        this.path = path;
        this.contents = contents;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
