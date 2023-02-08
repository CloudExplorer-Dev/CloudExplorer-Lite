package com.fit2cloud.provider.impl.openstack.api;

import com.fit2cloud.provider.entity.response.F2CVirtualMachine;
import com.fit2cloud.provider.impl.openstack.entity.request.ListEcsInstanceRequest;
import com.fit2cloud.provider.impl.openstack.entity.request.ListSecurityGroupInstanceRequest;
import com.fit2cloud.provider.impl.openstack.util.OpenStackUtils;
import org.apache.commons.lang3.StringUtils;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.network.SecurityGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  10:44}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class OpenstackApi {
    /**
     * 获取安全组列表
     *
     * @param request 请求对象
     * @return 安全组列表
     */
    public static List<SecurityGroup> listSecurityGroup(ListSecurityGroupInstanceRequest request) {
        List<SecurityGroup> list = new ArrayList<>();
        try {
            OSClient.OSClientV3 osClient = request.getCredential().getOSClient();
            osClient.useRegion(request.getRegionId());
            Map<String, String> filteringParams = new HashMap<>();
            //管理员账权限号可以拿到所有安全组？暂时无法区分是否为共享的，所以只拿这个project下的
            filteringParams.put("project_id", request.getCredential().getProject());

            for (SecurityGroup securityGroup : osClient.networking().securitygroup().list(filteringParams)) {
                //排除其他project下非共享的
                list.add(securityGroup);
            }


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 获取云主机列表
     *
     * @param request 请求对象
     * @return 云主机列表
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ListEcsInstanceRequest request) {
        List<F2CVirtualMachine> list = new ArrayList<>();
        try {
            OSClient.OSClientV3 osClient = request.getCredential().getOSClient();

            List<String> regions = OpenStackUtils.getRegionList(osClient);

            for (String region : regions) {
                if (request.getRegionId() != null && !StringUtils.equals(request.getRegionId(), region)) {
                    continue;
                }
                osClient.useRegion(region);
                List<? extends Server> instances = osClient.compute().servers().list(true);
                Map<String, Image> imageMap = osClient.imagesV2().list().stream().collect(Collectors.toMap(Image::getId, image -> image));
                for (Server instance : instances) {
                    list.add(OpenStackUtils.toF2CVirtualMachine(osClient, instance, region, imageMap));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return list;
    }
}
