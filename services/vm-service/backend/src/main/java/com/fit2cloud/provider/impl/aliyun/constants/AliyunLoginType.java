package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/16 6:08 PM
 */
public enum AliyunLoginType {

    Password("password","自定义密码"),
    KeyPair("keyPair","秘钥对");

    private String id;
    private String name;

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    AliyunLoginType(String id, String name){
        this.id = id;
        this.name = name;
    }
}
