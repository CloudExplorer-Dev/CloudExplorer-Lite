package com.fit2cloud.common.platform.credential;

import com.fit2cloud.common.form.annotaion.From;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    default List<Form> toForm() {
        return FormUtil.toForm(this.getClass());
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
    }
}
