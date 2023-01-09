package com.fit2cloud.provider.impl.tencent.entity.response;

import com.fit2cloud.provider.impl.tencent.parser.CeAbstractHandler;
import lombok.Data;
import org.xml.sax.Attributes;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  13:40}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

public class BucketEncryptionResponse extends CeAbstractHandler {
    private ServerSideEncryptionConfiguration response;

    @Data
    public static class ServerSideEncryptionConfiguration {
        private Rule rule;
    }

    @Data
    public static class Rule {
        private ApplyServerSideEncryptionByDefault applyServerSideEncryptionByDefault;
    }

    @Data
    public static class ApplyServerSideEncryptionByDefault {
        /**
         * 支持枚举值 AES256、KMS，AES256 代表使用 SSE-COS 模式，加密算法为 AES256；KMS 代表 SSE-KMS 模式
         */
        private String sseAlgorithm;
        /**
         * 当 SSEAlgorithm 的值为 KMS 时，用于指定 KMS 的用户主密钥 CMK，如不指定，则使用 COS 默认创建的 CMK，更多详细信息可参见 SSE-KMS 加密
         */
        private String kmsMasterKeyID;
    }

    @Override
    protected void doStartElement(String var1, String var2, String var3, Attributes var4) {
        this.response = new ServerSideEncryptionConfiguration();
        this.response.rule = new Rule();
        this.response.rule.applyServerSideEncryptionByDefault = new ApplyServerSideEncryptionByDefault();
    }

    @Override
    protected void doEndElement(String var1, String var2, String var3) {
        if (var2.equals("SSEAlgorithm")) {
            this.response.rule.applyServerSideEncryptionByDefault.sseAlgorithm = this.getText();
        }
        if (var2.equals("KMSMasterKeyID")) {
            this.response.rule.applyServerSideEncryptionByDefault.kmsMasterKeyID = this.getText();
        }
    }

    public ServerSideEncryptionConfiguration getResponse() {
        return this.response;
    }
}
