package com.fit2cloud.common.provider.impl.vsphere.api;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.impl.vsphere.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class VsphereBaseMethodApi {

    public static List<Credential.Region> getRegions(GetRegionsRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            try {
                VsphereCredential credential = JsonUtil.parseObject(request.getCredential(), VsphereCredential.class);
                return credential.regions();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>();
    }

}
