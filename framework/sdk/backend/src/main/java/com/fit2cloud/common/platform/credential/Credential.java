package com.fit2cloud.common.platform.credential;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
     * 将
     *
     * @return
     */
    default String enCode() {
        return JsonUtil.toJSONString(this);
    }

    /**
     * 获取form表单
     *
     * @return form表单
     */
    default List<? extends Form> toForm() {
        FormObject formObject = FormUtil.toForm(this.getClass());
        return formObject == null ? null: formObject.getForms();
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
