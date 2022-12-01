package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.JobBinary;

/**
 * Builder interface used for {@link JobBinary} object.
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobBinaryBuilder extends Builder<JobBinaryBuilder, JobBinary> {

    /**
     * See {@link JobBinary#getDescription()}
     *
     * @param description the description of the job binary
     * @return JobBinaryBuilder
     */
    JobBinaryBuilder description(String description);

    /**
     * See {@link JobBinary#getURL()}
     *
     * @param url the URL of the job binary
     * @return JobBinaryBuilder
     */
    JobBinaryBuilder url(String url);

    /**
     * See {@link JobBinary#getName()}
     *
     * @param name the name of the job binary
     * @return JobBinaryBuilder
     */
    JobBinaryBuilder name(String name);

    /**
     * See {@link JobBinary#getCredentials()}
     *
     * @param user     username of the credentials
     * @param password password of the credentials
     * @return JobBinaryBuilder
     */
    JobBinaryBuilder credentials(String user, String password);
}
