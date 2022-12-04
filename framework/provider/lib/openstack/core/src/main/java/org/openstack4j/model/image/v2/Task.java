package org.openstack4j.model.image.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.image.v2.builder.TaskBuilder;

import java.util.Date;
import java.util.Map;

/**
 * An object representing a Glance V2 task.
 * Tasks offer end users a front end to long
 * running asynchronous operations.
 *
 * @author emjburns
 */
public interface Task extends ModelEntity, Buildable<TaskBuilder> {

    /**
     * Date and time task was created.
     */
    Date getCreatedAt();

    /**
     * The date and time the task is subject to removal.
     * The result of the task will still exist.
     */
    Date getExpiresAt();

    /**
     * The date and time the task was updated.
     */
    Date getUpdatedAt();

    /**
     * Identifier for the task, a UUID.
     */
    String getId();

    /**
     * A JSON object specifying the input parameters of the task.
     */
    Map<String, Object> getInput();

    /**
     * Human readable text, possibly and empty string, usually
     * displayed in an error situation to provide more information
     * about what has occurred.
     */
    String getMessage();

    /**
     * Identifier for owner of the task, usually tenant ID.
     */
    String getOwner();

    /**
     * A JSON object specifying information about the ultimate
     * outcome of the task.
     */
    Map<String, String> getResult();

    /**
     * The URI for the schema describing an image task.
     */
    String getSchema();

    /**
     * The status of the task.
     *
     * @return taskStatus
     */
    TaskStatus getStatus();

    /**
     * The type of task represented by this content.
     */
    String getType();

    /**
     * A URI for this task.
     */
    String getSelf();

    public enum TaskStatus {
        /**
         * The task identifier has been reserved for a task in the Glance.
         * No processing has begun on it yet.
         */
        PENDING,
        /**
         * The task has been picked up by the underlying executor and is
         * being run using the backend Glance execution logic for that task type.
         */
        PROCESSING,
        /**
         * Denotes that the task has had a successful run within Glance.
         * The result field of the task shows more details about the outcome.
         */
        SUCCESS,
        /**
         * Denotes that an error occurred during the execution of the task and it cannot continue processing.
         * The message field of the task shows what the error was.
         */
        FAILURE,
        /**
         * Task status is not a known value.
         */
        UNKNOWN;

        @JsonCreator
        public static TaskStatus value(String v) {
            if (v == null) return UNKNOWN;
            try {
                return valueOf(v.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

}
