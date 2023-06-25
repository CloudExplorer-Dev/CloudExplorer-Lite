package com.fit2cloud.common.charging.policy.impl;

import com.fit2cloud.base.entity.ResourceInstanceState;
import com.fit2cloud.common.charging.policy.InstanceStatePolicy;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/25  11:13}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class DiskInstanceStatePolicy implements InstanceStatePolicy {

    private final String base_path = "/opt/cloudexplorer/data/instance_state";

    private final String state_month_path_format = "/opt/cloudexplorer/data/instance_state/%s/%s";

    private final String tmpFileNameFormat = "%s_%s_%s.json.tmp";
    private final String fileNameFormat = "%s_%s_%s.json";

    @Override
    public List<ResourceInstanceState> list(String cloudAccountId, String resourceType, String month) {
        String filePath = state_month_path_format.formatted(cloudAccountId, month);
        String fileName = fileNameFormat.formatted(cloudAccountId, resourceType, month);
        try {
            File file = new File(filePath + File.separator + fileName);
            if (file.exists()) {
                byte[] bytes = FileUtils.readFileToByteArray(file);
                return JsonUtil.parseArray(new String(bytes, StandardCharsets.UTF_8), ResourceInstanceState.class);
            } else {
                return new ArrayList<>();
            }

        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ResourceInstanceState> list(String resourceType, String month) {
        File file = new File(base_path);
        if (file.exists()) {
            File[] cloudAccountIdFileList = file.listFiles();
            if (Objects.nonNull(cloudAccountIdFileList)) {
                return Arrays.stream(cloudAccountIdFileList)
                        .flatMap(f -> list(f.getName(), resourceType, month)
                                .stream())
                        .toList();
            }
        } else {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @Override
    public void saveBatch(String resourceType, String month, List<ResourceInstanceState> states) {
        Map<String, List<ResourceInstanceState>> cloudAccountInstanceStateMap = states.stream()
                .collect(Collectors.groupingBy(ResourceInstanceState::getCloudAccountId));
        for (Map.Entry<String, List<ResourceInstanceState>> cloudAccountEntry : cloudAccountInstanceStateMap.entrySet()) {
            String filePath = state_month_path_format.formatted(cloudAccountEntry.getKey(), month);
            String data = JsonUtil.toJSONString(cloudAccountEntry.getValue());
            String fileName = fileNameFormat.formatted(cloudAccountEntry.getKey(), resourceType, month);
            String tmpFileName = tmpFileNameFormat.formatted(cloudAccountEntry.getKey(), resourceType, month);
            writeFileByByte(filePath, tmpFileName, fileName, data::getBytes);
        }

    }

    /**
     * 写入文件 先写入.tmp文件 ,然后原子性重命名 保证最终写入的是一个完整的json 不会因为程序宕机导致写入文件不完整
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @param getBytes 获取文件字节数据函数
     * @return 是否写入
     */
    public static File writeFileByByte(String filePath, String tmpFileName, String fileName, Supplier<byte[]> getBytes) {
        File tempFile = new File(filePath + "/" + tmpFileName);
        File file = new File(filePath + File.separator + fileName);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        try {
            // 文件未修改,不需要重新落盘
            FileUtils.writeByteArrayToFile(tempFile, getBytes.get(), false);
            // 写入完成重命名
            Files.move(tempFile.toPath(), file.toPath(), new StandardCopyOption[]{StandardCopyOption.ATOMIC_MOVE});
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
