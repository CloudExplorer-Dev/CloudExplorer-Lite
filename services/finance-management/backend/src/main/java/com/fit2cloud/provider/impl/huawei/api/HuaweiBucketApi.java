package com.fit2cloud.provider.impl.huawei.api;

import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.util.CsvUtil;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.constants.BillingSettingConstants;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiBillCredential;
import com.fit2cloud.provider.impl.huawei.entity.csv.HuaweiBillCsvModel;
import com.fit2cloud.provider.impl.huawei.entity.request.ListBucketMonthRequest;
import com.fit2cloud.provider.impl.huawei.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.huawei.util.HuaweiMappingUtil;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/24  10:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiBucketApi {

    /**
     * 月份文件
     */
    private static String billMonthFile = "PriceFactorBillDetail_\\d{8}-\\d{8}_\\d+.csv$";

    /**
     * 日文件
     */
    private static String billDayFile = "PriceFactorBillDetail_\\d{8}_\\d+.csv$";

    /**
     * 从存储桶获取华为云账单
     *
     * @param request 请求对象
     * @return 系统云账单
     */
    public static List<CloudBill> listBill(SyncBillRequest request) {
        ObsClient obsClient = request.getCredential().getObsClient(request.getBill().getRegionId());
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(request.getBill().getBucketId());
        List<ObsObject> obsObjects = listObsObject(obsClient, listObjectsRequest);
        // 是否存在月账单
        boolean exitsMonthFile = obsObjects.stream().anyMatch(obsObject -> Pattern.compile(billMonthFile).matcher(obsObject.getObjectKey().trim()).find() && obsObject.getObjectKey().contains(request.getCycle().replace("-", "").trim()));
        // 是否可读日账单
        boolean readDayBillFile = isReadDayBillFile(request.getCycle());
        List<HuaweiBillCsvModel> huaweiBillCsvModels = obsObjects.stream()
                .filter(obsObject -> (exitsMonthFile && Pattern.compile(billMonthFile).matcher(obsObject.getObjectKey().trim()).find()) || (!exitsMonthFile && readDayBillFile && Pattern.compile(billDayFile).matcher(obsObject.getObjectKey().trim()).find()))
                .filter(obsObject -> obsObject.getObjectKey().contains(request.getCycle().replace("-", "").trim()))
                .map(obsObject ->
                        CsvUtil.writeFile(BillingSettingConstants.billingPath + File.separator + new HuaweiCloudProvider().getCloudAccountMeta().platform + File.separator + request.getCloudAccountId(), obsObject.getObjectKey(),
                                obsObject.getMetadata().getContentLength(), () -> obsClient.getObject(new GetObjectRequest() {{
                                    setBucketName(obsObject.getBucketName());
                                    setObjectKey(obsObject.getObjectKey());
                                }}).getObjectContent()))
                .flatMap(file -> CsvUtil.parse(file, HuaweiBillCsvModel.class).stream())
                .toList();
        return huaweiBillCsvModels.stream().map(huaweiBillCsvModel -> HuaweiMappingUtil.toCloudBill(huaweiBillCsvModel, request)).toList();
    }

    /**
     * 是否读取日账单
     *
     * @param month 月份
     * @return 是否
     */
    private static boolean isReadDayBillFile(String month) {
        return MonthUtil.getHistoryMonth(2).contains(month);
    }


    /**
     * 获取桶中所有文件
     *
     * @param credential 华为云认证信息
     * @param bucketName 桶名称
     * @return 桶中所有数据
     */
    public static List<ObsObject> listObsObject(HuaweiBillCredential credential, String bucketName) {
        ObsClient obsClient = credential.getObsClient();
        ObsBucket obs = obsClient.listBuckets(new ListBucketsRequest()).stream().filter(obsBucket -> obsBucket.getBucketName().equals(bucketName)).findFirst().orElseThrow(() -> new RuntimeException("桶不存在"));
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setMaxKeys(1000);
        return listObsObject(credential.getObsClient(obs.getLocation()), listObjectsRequest);
    }

    /**
     * 获取桶中所有数据
     *
     * @param request   请求数据
     * @param obsClient 客户端
     * @return 桶中所有数据
     */
    private static List<ObsObject> listObsObject(ObsClient obsClient, ListObjectsRequest request) {
        return PageUtil.page(request,
                obsClient::listObjects,
                ObjectListing::getObjects,
                (req, res) -> StringUtils.isNotEmpty(res.getNextMarker()),
                (req, res) -> req.setMarker(res.getNextMarker()));
    }


    /**
     * 获取所有的桶中文件,根据月份
     *
     * @param listBucketMonthRequest 请求对象
     * @return 文件名称列表
     */
    public static List<String> listBucketFileMonth(ListBucketMonthRequest listBucketMonthRequest) {
        List<ObsObject> obsObjects = listObsObject(listBucketMonthRequest.getCredential(), listBucketMonthRequest.getBill().getBucketId());
        return obsObjects.stream().map(ObsObject::getObjectKey)
                .filter(key -> Pattern.compile(billDayFile).matcher(key).find() || Pattern.compile(billMonthFile).matcher(key).find())
                .map(key -> {
                    String month = findMonth(key, billMonthFile);
                    return StringUtils.isNotEmpty(month) ? month : findMonth(key, billDayFile);
                }).distinct().toList();
    }

    /**
     * 根据文件名称查询月份
     *
     * @param fileName    文件名称
     * @param filePattern 正则
     * @return 月份 yyyy-mm
     */
    private static String findMonth(String fileName, String filePattern) {
        Matcher monthFileNameMatcher = Pattern.compile(filePattern).matcher(fileName);
        if (monthFileNameMatcher.find()) {
            String monthFileName = monthFileNameMatcher.group();
            Matcher monthMatcher = Pattern.compile("\\d{8}").matcher(monthFileName);
            if (monthMatcher.find()) {
                String month = monthMatcher.group();
                return month.substring(0, 4) + "-" + month.substring(4, 6);
            }
        }
        return null;
    }
}
