package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * Represents the get Console Output action with related parameters.
 * It is used to query the console output of a server.
 *
 * @author Lorenzo Biava
 */
@JsonRootName("os-getConsoleOutput")
public class ConsoleOutputOptions implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("length")
    private Integer length;

    /**
     * To get the full console output.
     */
    public ConsoleOutputOptions() {
        this.length = null;
    }

    /**
     * To get the last <code>length</code> lines of the console output.
     *
     * @param length the number of lines to retrieve.
     */
    public ConsoleOutputOptions(Integer length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

}
