package com.fit2cloud.provider.impl.tencent.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/16 6:08 PM
 */
public enum TencentLoginType {

    Password("password","自定义密码");

    private String id;
    private String name;

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    TencentLoginType(String id, String name){
        this.id = id;
        this.name = name;
    }
}
