package com.fit2cloud.provider.impl.tencent.api;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.util.CsvUtil;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.constants.BillingSettingConstants;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.tencent.entity.csv.TencentCsvModel;
import com.fit2cloud.provider.impl.tencent.entity.request.ListBucketMonthRequest;
import com.fit2cloud.provider.impl.tencent.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/28  9:47}
 * {@code @Version 1.0}
 * {@code @注释:  }
 */
public class TencentBucketApi {
    /**
     * 日账单
     */
    private final static Pattern billDayPattern = Pattern.compile("-\\d{8}-按扣费周期-明细账单.zip$");
    /**
     * 月账单
     */
    private final static Pattern billMonthPattern = Pattern.compile("-\\d{6}-按扣费周期-明细账单.zip$");


    /**
     * 获取账单 从桶中获取账单
     *
     * @param request 请求对象
     * @return 账单数据
     */
    public static List<CloudBill> listBill(SyncBillRequest request) {
        COSClient cosClient = request.getCredential().getCOSClient(request.getBill().getRegionId());
        List<COSObjectSummary> cosObjectSummaries = listCOSObjectSummary(cosClient, request.getBill().getBucketId());
        // 是否存在月账单
        boolean exitsMonthFile = cosObjectSummaries.stream().anyMatch(obsObject -> billMonthPattern.matcher(obsObject.getKey().trim()).find() && obsObject.getKey().contains(request.getMonth().replace("-", "").trim()));
        // 是否可读日账单
        boolean readDayBillFile = isReadDayBillFile(request.getMonth());
        List<Credential.Region> regions = request.getCredential().regions();
        // 过滤可读账单
        Map<String, Optional<COSObjectSummary>> groups = cosObjectSummaries.stream()
                .filter(cosObjectSummary -> (exitsMonthFile && billMonthPattern.matcher(cosObjectSummary.getKey()).find()) || (!exitsMonthFile && readDayBillFile && billDayPattern.matcher(cosObjectSummary.getKey()).find()))
                .filter(obsObject -> obsObject.getKey().contains(request.getMonth().replace("-", "").trim()))
                .collect(Collectors.groupingBy(cosObjectSummary -> cosObjectSummary.getKey().replaceAll(billDayPattern.pattern(), "").replaceAll(billMonthPattern.pattern(), ""),
                        Collectors.reducing((pre, next) -> pre.getKey().compareTo(next.getKey()) > 0 ? pre : next)));
        return groups.entrySet().stream().flatMap(entry ->
                entry.getValue()
                        .map(cosObjectSummary ->
                                CsvUtil.writeFile(
                                        BillingSettingConstants.billingPath + File.separator + PlatformConstants.fit2cloud_tencent_platform.name() + File.separator + request.getCloudAccountId(),
                                        cosObjectSummary.getKey(),
                                        cosObjectSummary.getSize(),
                                        () -> {
                                            COSObject object = cosClient.getObject(cosObjectSummary.getBucketName(), cosObjectSummary.getKey());
                                            return object.getObjectContent();
                                        })
                        ).map(file -> fileToCloudBill(file, request, regions)).stream().flatMap(Collection::stream)
        ).toList();

    }


    /**
     * @param file    账单文件对象
     * @param request 请求对象
     * @param regions 区域对象
     * @return 系统账单
     */
    private static List<CloudBill> fileToCloudBill(File file, SyncBillRequest request, List<Credential.Region> regions) {
        try {
            InputStream input = new FileInputStream(file);
            ZipFile zipFile = new ZipFile(file);
            ZipInputStream zipInputStream = new ZipInputStream(input);
            List<TencentCsvModel> tencentCsvModels = new ArrayList<>();
            ZipEntry nextEntry;
            while ((nextEntry = zipInputStream.getNextEntry()) != null) {
                List<TencentCsvModel> parse = CsvUtil.parse(zipFile.getInputStream(nextEntry), TencentCsvModel.class);
                Map<String, Map<String, String>> resourceTag = getTagMap("资源ID", zipFile.getInputStream(nextEntry));
                for (TencentCsvModel tencentCsvModel : parse) {
                    Map<String, String> tag = resourceTag.get(tencentCsvModel.getResourceId());
                    tencentCsvModel.setTag(tag);
                }
                tencentCsvModels.addAll(parse);
            }
            return tencentCsvModels.stream().map(model -> TencentMappingUtil.toCloudBillByBucket(model, regions, request)).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否读取日账单
     *
     * @param month 月份
     * @return 是否
     */
    public static boolean isReadDayBillFile(String month) {
        return MonthUtil.getHistoryMonth(2).contains(month);
    }

    /**
     * 获取到制定桶中所有对象
     *
     * @param cosClient  客户端
     * @param bucketName 桶对象
     * @return 桶中所有文件对象
     */
    public static List<COSObjectSummary> listCOSObjectSummary(COSClient cosClient, String bucketName) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setMaxKeys(1000);
        listObjectsRequest.setBucketName(bucketName);
        cosClient.listObjects(listObjectsRequest);
        return PageUtil.page(listObjectsRequest,
                cosClient::listObjects,
                ObjectListing::getObjectSummaries,
                (req, res) -> StringUtils.isNotEmpty(res.getNextMarker()),
                (req, res) -> req.setMarker(res.getNextMarker()));

    }

    /**
     * 获取账单标签
     *
     * @param keyField    每一行的唯一标识字段
     * @param inputStream 账单数据流
     * @return key:每一行字段 value: 每一行的标签
     */
    private static Map<String, Map<String, String>> getTagMap(String keyField, InputStream inputStream) {
        try {
            List<List<String>> csvList = readCsvFile(inputStream);
            List<String> titles = csvList.get(0);
            return IntStream.range(0, titles.size()).boxed().filter(keyIndex -> titles.get(keyIndex).equals(keyField)).findFirst().map(index -> {
                // 获取key
                List<Integer> tagsIndex = IntStream.range(0, titles.size()).filter(tagIndex -> titles.get(tagIndex).startsWith("标签键")).boxed().toList();
                return csvList.stream().skip(1).flatMap(cs -> {
                    return tagsIndex.stream().map(tagIndex -> {
                                // value为标签值
                                String value = cs.get(tagIndex).replace("\"", "");
                                if (value.equals("-")) {
                                    return null;
                                }
                                //  key为资源id
                                String key = cs.get(index);
                                key = key.replaceAll("\"", "");
                                Map<String, String> stringValues = new HashMap<>();
                                stringValues.put(titles.get(tagIndex).replace("标签键:", ""), value);
                                return new DefaultKeyValue<>(key, stringValues);
                            }).filter(Objects::nonNull)
                            .collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue)).entrySet().stream();
                }).filter(Objects::nonNull).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> {
                    e1.putAll(e2);
                    return e1;
                }));
            }).orElseThrow(() -> new Fit2cloudException(ErrorCodeConstants.BILL_PROVIDER_TENCENT_CLOUD_KEY_NOT_EXIST.getCode(), ErrorCodeConstants.BILL_PROVIDER_TENCENT_CLOUD_KEY_NOT_EXIST.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 读取账单数据
     *
     * @param inputStream 长大数据流
     * @return csv数据对象
     * @throws IOException
     */
    public static List<List<String>> readCsvFile(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String tmp = null;
        List<List<String>> response = new ArrayList<>();
        while ((tmp = bufferedReader.readLine()) != null) {
            String[] split = tmp.split(",");
            response.add(Arrays.stream(split).collect(Collectors.toList()));
        }
        return response;
    }


    /**
     * 获取桶文件中的文件月份
     *
     * @param request 请求对象
     * @return 桶中文件所有月份
     */
    public static List<String> listBucketFileMonth(ListBucketMonthRequest request) {
        COSClient cosClient = request.getCredential().getCOSClient(request.getBill().getRegionId());
        List<COSObjectSummary> cosObjectSummaries = listCOSObjectSummary(cosClient, request.getBill().getBucketId());
        List<String> months = cosObjectSummaries.stream().map(COSObjectSummary::getKey).filter(key -> billMonthPattern.matcher(key).find() || billDayPattern.matcher(key).find()).map(key -> {
            Matcher monthPattern = billMonthPattern.matcher(key);
            if (monthPattern.find()) {
                String billMonth = monthPattern.group();
                Matcher matcher = Pattern.compile("\\d{6}").matcher(billMonth);
                if (matcher.find()) {
                    String month = matcher.group();
                    return month.substring(0, 4) + "-" + month.substring(4, 6);
                }
            }
            Matcher dayMatcher = billDayPattern.matcher(key);
            if (dayMatcher.find()) {
                String billDay = dayMatcher.group();
                Matcher matcher = Pattern.compile("\\d{8}").matcher(billDay);
                if (matcher.find()) {
                    String month = matcher.group();
                    return month.substring(0, 4) + "-" + month.substring(4, 6);
                }
            }
            return null;
        }).filter(Objects::nonNull).distinct().toList();
        return months;
    }


}
