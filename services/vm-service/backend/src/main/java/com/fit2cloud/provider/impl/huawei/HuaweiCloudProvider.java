package com.fit2cloud.provider.impl.huawei;

import com.fit2cloud.common.platform.credential.impl.HuaweiCredential;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  11:06 AM
 * @Version 1.0
 * @注释:
 */
public class HuaweiCloudProvider extends AbstractCloudProvider<HuaweiCredential> implements ICloudProvider {
    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return null;
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return null;
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return null;
    }
}
