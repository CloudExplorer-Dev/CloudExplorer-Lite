package com.fit2cloud.provider.impl.aliyun.entity.response;

import com.aliyun.vpc20160428.models.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  16:08}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class VpcInstanceResponse extends DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc {
    /**
     * vpn网关
     */
    private DescribeVpnGatewaysResponseBody.DescribeVpnGatewaysResponseBodyVpnGatewaysVpnGateway vpnGateway;

    /**
     * 连接信息
     */
    private DescribeVpnConnectionsResponseBody.DescribeVpnConnectionsResponseBodyVpnConnectionsVpnConnection vpnConnection;

    /**
     * 流日志数据
     */
    private DescribeFlowLogsResponseBody.DescribeFlowLogsResponseBodyFlowLogsFlowLog flowLog;

    /**
     * 交换机
     */
    private List<DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch> switchesList;

    public VpcInstanceResponse(DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc vpcInstance) {
        BeanUtils.copyProperties(vpcInstance, this);
    }
}
