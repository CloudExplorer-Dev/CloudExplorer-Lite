package org.openstack4j.model.storage.object.options;

import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * List options used for Object based queries
 *
 * @author Jeremy Unruh
 */
public final class ObjectListOptions {

    private Map<String, String> queryParams = Maps.newHashMap();

    private ObjectListOptions() {
    }

    public static ObjectListOptions create() {
        return new ObjectListOptions();
    }

    /**
     * list operation returns no more than this amount.
     */
    public ObjectListOptions limit(int limit) {
        checkState(limit >= 0, "limit must be >= 0");
        checkState(limit <= 10000, "limit must be <= 10000");
        queryParams.put("limit", Integer.toString(limit));
        return this;
    }

    /**
     * Objects greater in value than the specified marker are returned.
     */
    public ObjectListOptions marker(String marker) {
        queryParams.put("marker", checkNotNull(marker, "marker"));
        return this;
    }

    /**
     * Objects less in value than the specified marker are returned.
     */
    public ObjectListOptions endMarker(String endMarker) {
        queryParams.put("end_marker", checkNotNull(endMarker, "endMarker"));
        return this;
    }

    /**
     * Objects beginning with this substring are returned.
     */
    public ObjectListOptions startsWith(String prefix) {
        queryParams.put("prefix", checkNotNull(prefix, "prefix"));
        return this;
    }

    /**
     * Objects nested in the container are returned.
     */
    public ObjectListOptions delimiter(char delimiter) {
        queryParams.put("delimiter", Character.toString(delimiter));
        return this;
    }

    /**
     * Objects nested in the pseudo path are returned.
     */
    public ObjectListOptions path(String path) {
        queryParams.put("path", checkNotNull(path, "path"));
        return this;
    }

    public Map<String, String> getOptions() {
        return queryParams;
    }

}
