package com.fit2cloud.common.provider;


import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.entity.ActionInfo;
import com.fit2cloud.common.provider.entity.F2CBalance;
import org.jetbrains.annotations.NotNull;
import org.pf4j.ExtensionPoint;

import java.util.List;
import java.util.Map;


public interface IBaseCloudProvider extends ExtensionPoint {

    /**
     * 获取云账号余额
     *
     * @param getAccountBalanceRequest 请求对象
     * @return 账户余额
     */
    F2CBalance getAccountBalance(String getAccountBalanceRequest);


    /**
     * 获取云账号
     *
     * @return 云平台元数据
     */
    CloudAccountMeta getCloudAccountMeta();

    /**
     * 插件函数 信息
     *
     * @return info数据
     */
    Info getInfo();

    static class Info {
        /**
         * 模块
         */
        public final String module;
        /**
         * 当前插件支持的操作  所有插件都必须表明可执行的操作
         */
        public final List<? extends ActionInfo> supportActionList;
        /**
         * 其他元数据存储
         */
        public final Map<String, Object> meta;

        public Info(String module, List<? extends ActionInfo> supportActionList, @NotNull Map<String, Object> meta) {
            this.module = module;
            this.supportActionList = supportActionList;
            this.meta = meta;

        }
    }

    static class CloudAccountMeta {
        /**
         * 认证对象
         */
        public final Class<? extends Credential> credential;
        /**
         * 云账号提示
         */
        public final String message;

        /**
         * 云账号唯一标示
         */
        public final String platform;
        /**
         * 是否是公有云
         */
        public final boolean publicCloud;
        /**
         * 云平台logo
         */
        public final String logoSvgString;
        /**
         * 云平台icon
         */
        public final String iconSvgString;
        /**
         * 其他元数据存储
         */
        public final Map<String, Object> meta;

        public CloudAccountMeta(Class<? extends Credential> credential,
                                String platform,
                                String message,
                                boolean publicCloud,
                                String logoSvgString,
                                String iconSvgString,
                                Map<String, Object> meta) {
            this.credential = credential;
            this.platform = platform;
            this.message = message;
            this.publicCloud = publicCloud;
            this.logoSvgString = logoSvgString;
            this.iconSvgString = iconSvgString;
            this.meta = meta;
        }
    }

}
