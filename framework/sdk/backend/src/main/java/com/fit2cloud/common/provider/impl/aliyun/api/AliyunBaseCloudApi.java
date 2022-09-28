package com.fit2cloud.common.provider.impl.aliyun.api;


import com.aliyun.bssopenapi20171214.models.GetAccountRelationResponse;
import com.aliyun.bssopenapi20171214.models.QueryAccountBalanceResponse;
import com.aliyun.bssopenapi20171214.models.QueryAccountBalanceResponseBody;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.aliyun.entity.credential.AliyunBaseCredential;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

public class AliyunBaseCloudApi {

    /**
     * 获取云账户余额
     *
     * @param request
     * @return
     */
    public static F2CBalance getAccountBalance(GetAccountBalanceRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            try{
                AliyunBaseCredential credential = JsonUtil.parseObject(request.getCredential(), AliyunBaseCredential.class);
                com.aliyun.bssopenapi20171214.Client bssClient = credential.getBssClient();
                QueryAccountBalanceResponse response = bssClient.queryAccountBalanceWithOptions(new com.aliyun.teautil.models.RuntimeOptions());
                QueryAccountBalanceResponseBody.QueryAccountBalanceResponseBodyData data = response.getBody().getData();
                return new F2CBalance(Double.parseDouble(data.availableAmount), "", 0.00);
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }
        return new F2CBalance();
    }

}
