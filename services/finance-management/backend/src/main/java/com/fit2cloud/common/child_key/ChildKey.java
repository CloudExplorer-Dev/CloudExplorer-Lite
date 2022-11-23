package com.fit2cloud.common.child_key;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

public interface ChildKey {
    /**
     * 获取子字段
     *
     * @return 所有的子字段
     */
    List<DefaultKeyValue<String, String>> childKeys();
}
