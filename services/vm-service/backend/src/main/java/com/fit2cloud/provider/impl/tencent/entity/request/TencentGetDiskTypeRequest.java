package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.entity.TencentInstanceType;
import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.tencentcloudapi.cbs.v20170312.models.DescribeDiskConfigQuotaRequest;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/11/28 10:49
 */

@Data
public class TencentGetDiskTypeRequest extends TencentBaseRequest {
    /**
     * 系统盘 or 数据盘
     */
    String diskUsage;

    String zoneId;

    /**
     * 付费类型
     */
    String instanceChargeType;

    /**
     * 实例类型
     */
    TencentInstanceType instanceTypeDTO;

    public DescribeDiskConfigQuotaRequest toDescribeDiskConfigQuotaRequest() {
        DescribeDiskConfigQuotaRequest describeDiskConfigQuotaRequest = new DescribeDiskConfigQuotaRequest();
        describeDiskConfigQuotaRequest.setDiskUsage(this.diskUsage);
        describeDiskConfigQuotaRequest.setInquiryType("INQUIRY_CVM_CONFIG");
        describeDiskConfigQuotaRequest.setDiskChargeType(TencentMappingUtil.toTencentChargeType(this.instanceChargeType));

        if (this.zoneId != null) {
            describeDiskConfigQuotaRequest.setZones(new String[]{this.zoneId});
        }

        if (this.instanceTypeDTO != null) {
            String instanceTypeFamily = instanceTypeDTO.getInstanceTypeFamily() == null ?
                    this.instanceTypeDTO.getInstanceType().substring(0, this.instanceTypeDTO.getInstanceType().indexOf(".")) : instanceTypeDTO.getInstanceTypeFamily();
            if (instanceTypeFamily != null) {
                describeDiskConfigQuotaRequest.setInstanceFamilies(new String[]{instanceTypeFamily});
            }
            describeDiskConfigQuotaRequest.setCPU(this.instanceTypeDTO.getCpu());
            describeDiskConfigQuotaRequest.setMemory(this.instanceTypeDTO.getMemory());
        }
        return describeDiskConfigQuotaRequest;
    }
}
