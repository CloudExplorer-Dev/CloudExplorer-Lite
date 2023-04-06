package com.fit2cloud.common.child_key.impl;

import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.common.child_key.ChildKey;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;
import java.util.stream.IntStream;

public class OrgTreeChildKey implements ChildKey {
    @Override
    public List<DefaultKeyValue<String, String>> childKeys() {
        IBaseOrganizationService organizationService = SpringUtil.getBean(IBaseOrganizationService.class);
        int orgLevel = organizationService.getOrgLevel();
        return IntStream.range(1, orgLevel + 1).boxed().map(level -> {
            String label = level + "级组织";
            String value = "orgTree." + label;
            return new DefaultKeyValue<>(label, value);
        }).toList();
    }
}
