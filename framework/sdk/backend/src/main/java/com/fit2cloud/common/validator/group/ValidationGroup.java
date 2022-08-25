package com.fit2cloud.common.validator.group;

import javax.validation.groups.Default;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  6:37 PM
 * @Version 1.0
 * @注释:
 */
public class ValidationGroup {
    // 查询
    public interface SELECT extends Default {
    }

    ;
   // 插入
   public interface SAVE extends Default  {
    }

    ;
    // 修改
    public interface UPDATE extends Default  {
    }

    ;
   // 删除
   public  interface DELETE extends Default {
    }
    ;
}

