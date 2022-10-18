package com.fit2cloud.provider;


import com.fit2cloud.es.entity.CloudBill;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:34 AM
 * @Version 1.0
 * @注释:
 */
public interface ICloudProvider {
    List<CloudBill> syncBill(String request);
}
