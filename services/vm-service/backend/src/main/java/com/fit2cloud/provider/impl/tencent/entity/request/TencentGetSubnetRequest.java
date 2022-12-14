package com.fit2cloud.provider.impl.tencent.entity.request;

import com.tencentcloudapi.vpc.v20170312.models.DescribeSubnetsRequest;
import com.tencentcloudapi.vpc.v20170312.models.Filter;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiuDi
 * @date : 2022/11/28 14:03
 */
@Data
public class TencentGetSubnetRequest extends TencentBaseRequest {

    private String zoneId;

    private String vpcId;

    public DescribeSubnetsRequest toDescribeSubnetsRequest() {
        DescribeSubnetsRequest req = new DescribeSubnetsRequest();
        List<com.tencentcloudapi.vpc.v20170312.models.Filter> filterList = new ArrayList<>();
        if (StringUtils.isNotBlank(zoneId)) {
            Filter zoneFilter = new Filter();
            zoneFilter.setName("zone");
            zoneFilter.setValues(new String[]{zoneId});
            filterList.add(zoneFilter);
        }
        if (StringUtils.isNotBlank(vpcId)) {
            Filter vpcIdFilter = new Filter();
            vpcIdFilter.setName("vpc-id");
            vpcIdFilter.setValues(new String[]{vpcId});
            filterList.add(vpcIdFilter);
        }
        if (CollectionUtils.isNotEmpty(filterList)) {
            req.setFilters(filterList.toArray(new Filter[filterList.size()]));
        }
        return req;
    }
}
