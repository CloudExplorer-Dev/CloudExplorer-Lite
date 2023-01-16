package com.fit2cloud.provider.impl.huawei.api;

import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.util.FieldUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/4  11:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiInstanceSearchFieldApi {
    /**
     * 云服务器实例前缀
     */
    public static final String ECS_INSTANCE_PREFIX = "instance.fit2cloud_huawei_platform_ECS.";

    /**
     * 对象存储实例前缀
     */
    public static final String OSS_INSTANCE_PREFIX = "instance.fit2cloud_huawei_platform_OSS.";

    public static List<InstanceSearchField> listEcsInstanceSearchField() {

        return List.of();
    }

    public static List<InstanceSearchField> listOssInstanceSearchField() {
        InstanceSearchField encryption = new InstanceSearchField("加密方式", "encryption.sseAlgorithm.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("KMS", "KMS"),
                        new DefaultKeyValue<>("未开启", null)
                ));
        InstanceSearchField accessIdentifier = new InstanceSearchField("acl拥有者", "acl.grants.grantee.identifier.keyword", InstanceFieldType.ArrayString);
        return FieldUtil.appendPrefixField(OSS_INSTANCE_PREFIX, List.of(encryption, accessIdentifier));
    }
}
