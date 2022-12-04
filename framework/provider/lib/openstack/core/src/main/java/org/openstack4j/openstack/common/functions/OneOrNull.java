package org.openstack4j.openstack.common.functions;

import com.google.common.base.Function;

import java.util.List;

/**
 * A Function which returns one entry from a List or null
 *
 * @param <T> The return Type
 * @author Jeremy Unruh
 */
public class OneOrNull<T> implements Function<List<T>, T> {

    public static <T> OneOrNull<T> create() {
        return new OneOrNull<T>();
    }

    @Override
    public T apply(List<T> input) {
        if (input != null && input.size() > 0)
            return input.get(0);
        return null;
    }

}
