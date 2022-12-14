package com.fit2cloud.common.platform.credential;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/6  2:53 PM
 * @Version 1.0
 * @注释:
 */
public interface Credential {
    /**
     * 将json字符串解析为凭证对象
     *
     * @return 凭证对象
     */

    default Credential deCode(String credential) {
        return JsonUtil.parseObject(credential, this.getClass());
    }

    /**
     * 将当前对象转换序列化为字符串
     *
     * @return 序列化后的字符串
     */
    default String enCode() {
        return JsonUtil.toJSONString(this);
    }

    /**
     * 根据供应商获取认证class
     *
     * @param platform 供应商
     * @return 认证Class
     */
    static Class<? extends Credential> of(String platform) {
        PlatformConstants platformConstants = Arrays.stream(PlatformConstants.values()).filter(p -> p.name().equals(platform)).findFirst().orElseThrow(() -> new Fit2cloudException(404, "不支持的认证对象"));
        return platformConstants.getCredentialClass();
    }

    /**
     * 根据供应商和认证信息获取认证对象
     *
     * @param platform   供应商
     * @param credential 认证信息
     * @return 认证对象
     */
    static Credential of(String platform, String credential) {
        try {
            return of(platform).getConstructor().newInstance().deCode(credential);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取form表单
     *
     * @return form表单
     */
    default List<? extends Form> toForm() {
        FormObject formObject = FormUtil.toForm(this.getClass());
        return formObject == null ? null : formObject.getForms();
    }

    /**
     * 校验函数
     */
    boolean verification();

    /**
     * 获取区域/数据中心
     *
     * @return 区域
     */
    List<Region> regions();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class Region implements Serializable {
        /**
         * 区域id/数据中心id
         */
        private String regionId;
        /**
         * 区域名称/数据中心名称
         */
        private String name;

        /**
         * 访问域名
         */
        private String endpoint;
    }
}
