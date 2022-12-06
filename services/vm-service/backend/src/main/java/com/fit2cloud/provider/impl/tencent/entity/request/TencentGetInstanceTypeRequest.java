package com.fit2cloud.provider.impl.tencent.entity.request;

import com.tencentcloudapi.cvm.v20170312.models.DescribeInstanceTypeConfigsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZoneInstanceConfigInfosRequest;
import com.tencentcloudapi.cvm.v20170312.models.Filter;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiuDi
 * @date : 2022/11/25 17:20
 */
@Data
public class TencentGetInstanceTypeRequest extends TencentBaseRequest {
    private String instanceChargeType;
    private String zoneId;

    public DescribeZoneInstanceConfigInfosRequest toDescribeZoneInstanceConfigInfosRequest() {
        DescribeZoneInstanceConfigInfosRequest req = new DescribeZoneInstanceConfigInfosRequest();

        List<Filter> filterList = new ArrayList<>();
        if (StringUtils.isNotBlank(this.zoneId)) {
            Filter filter = new Filter();
            filter.setName("zone");
            filter.setValues(new String[]{zoneId});
            filterList.add(filter);
        }

        if (StringUtils.isNotBlank(this.instanceChargeType)) {
            Filter filter = new Filter();
            filter.setName("instance-charge-type");
            filter.setValues(new String[]{instanceChargeType});
            filterList.add(filter);
        }

        Filter sortFilter1 = new Filter();
        sortFilter1.setName("sort-keys");
        sortFilter1.setValues(new String[]{"instanceFamily:desc"});

        Filter sortFilter2 = new Filter();
        sortFilter2.setName("sort-keys");
        sortFilter2.setValues(new String[]{"instanceType:asc"});

        Filter sortFilter3 = new Filter();
        sortFilter3.setName("sort-keys");
        sortFilter3.setValues(new String[]{"cpu:asc"});

        Filter sortFilter4 = new Filter();
        sortFilter4.setName("sort-keys");
        sortFilter4.setValues(new String[]{"memory:asc"});

        filterList.add(sortFilter1);
        filterList.add(sortFilter2);
        filterList.add(sortFilter3);
        filterList.add(sortFilter4);

        if (CollectionUtils.isNotEmpty(filterList)) {
            req.setFilters(filterList.toArray(new Filter[filterList.size()]));
        }
        return req;
    }
}
