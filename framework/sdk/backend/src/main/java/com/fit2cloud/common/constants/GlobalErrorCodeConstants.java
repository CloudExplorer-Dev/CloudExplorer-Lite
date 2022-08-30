package com.fit2cloud.common.constants;

/**
 * @Author:张少虎
 * @Date: 2022/8/29  5:54 PM
 * @Version 1.0
 * @注释:
 */
public enum GlobalErrorCodeConstants {
    ORGANIZATION_CANNOT_DELETE(10010,"不能删除一个父级组织,请先删除子组织"),
    ORGANIZATION_ID_AND_NAME_REQUIRED(100011,"组织和名称必须传一个");
    /**
     * 提示
     */
    private String message;
    /**
     * 状态吗
     */
    private Integer code;

    GlobalErrorCodeConstants(Integer code,String message){
      this.code=code;
      this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
