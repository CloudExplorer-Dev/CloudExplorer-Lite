package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.bssopenapi20171214.models.DescribePricingModuleRequest;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunChargeType;
import lombok.Data;

@Data
public class AliyunGetPriceModuleRequest extends AliyunBaseRequest {
    private String instanceChargeType;

    public DescribePricingModuleRequest toDescribePricingModuleRequest() {
        // 默认查ECS产品，后续有其他产品需要再改造
        DescribePricingModuleRequest describePricingModuleRequest = new DescribePricingModuleRequest();
        describePricingModuleRequest.setProductType("");
        describePricingModuleRequest.setProductCode("ecs");
        describePricingModuleRequest.setSubscriptionType(AliyunChargeType.PREPAID.getId().equalsIgnoreCase(this.instanceChargeType) ? "Subscription" : "PayAsYouGo");
        return describePricingModuleRequest;
    }
}
