package com.fit2cloud.common.child_key.impl;

import com.fit2cloud.common.child_key.ChildKey;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.ArrayList;
import java.util.List;

public class BaseChildKey implements ChildKey {
    @Override
    public List<DefaultKeyValue<String, String>> childKeys( ) {
        return new ArrayList<>();
    }
}
