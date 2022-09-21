package com.fit2cloud.provider.impl.aliyun;

import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.exception.ReTryException;
import com.fit2cloud.provider.impl.aliyun.api.AliyunSyncCloudApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListDisksRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListImageRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListInstanceTypesRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListVirtualMachineRequest;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.util.PageUtil;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:55 AM
 * @Version 1.0
 * @注释:
 */
public class AliyunCloudProvider extends AbstractCloudProvider<AliyunVmCredential> implements ICloudProvider {


    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return AliyunSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return AliyunSyncCloudApi.listImage(JsonUtil.parseObject(req, ListImageRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return AliyunSyncCloudApi.listDisk(JsonUtil.parseObject(req, ListDisksRequest.class));
    }


}
