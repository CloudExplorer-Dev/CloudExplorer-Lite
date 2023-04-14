package com.fit2cloud.common.util;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.function.Supplier;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/22  10:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CsvUtil {

    /**
     * 解析csv
     *
     * @param file 需要解析的文件
     * @param type 需要解析的实体对象
     * @param <T>  实体对象类型
     * @return csv数据
     */
    @SneakyThrows
    public static <T> List<T> parse(File file, Class<T> type) {
        return new CsvToBeanBuilder<T>(new CSVReader(new InputStreamReader(getInputStream(new FileInputStream(file)))))
                .withType(type)
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .build()
                .parse();
    }

    /**
     * 解析csv
     *
     * @param in   需要解析的数据流
     * @param type 需要解析的实力列席
     * @param <T>  实体对象类型
     * @return csv数据
     */
    @SneakyThrows
    public static <T> List<T> parse(InputStream in, Class<T> type) {
        return new CsvToBeanBuilder<T>(new CSVReader(new InputStreamReader(getInputStream(in))))
                .withType(type)
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .build()
                .parse();
    }


    /**
     * 处理csv第一列读取不到的问题
     * 读取流中前面的字符，看是否有bom，如果有bom，将bom头先读掉丢弃
     *
     * @param in 数据流
     * @return 处理后的输入流
     * @throws IOException 文件格式不是utf-8则报错
     */
    public static InputStream getInputStream(InputStream in) throws IOException {
        PushbackInputStream pin = new PushbackInputStream(in);
        int ch = pin.read();
        if (ch != 0xEF) {
            pin.unread(ch);
        } else if ((ch = pin.read()) != 0xBB) {
            pin.unread(ch);
            pin.unread(0xef);
        } else if ((ch = pin.read()) != 0xBF) {
            throw new IOException("错误的UTF-8格式文件");
        }
        return pin;
    }


    /**
     * @param filePath       文件路径
     * @param fileName       文件名称
     * @param cloudFileSeize 文件大小
     * @param inputStream    文件输入流
     * @return 是否写入
     */
    public static File writeFile(String filePath, String fileName, Long cloudFileSeize, InputStream inputStream) {
        return writeFile(filePath, fileName, cloudFileSeize, () -> inputStream);
    }

    /**
     * @param filePath       文件路径
     * @param fileName       文件名称
     * @param cloudFileSeize 文件大小
     * @param bytes          文件字节
     * @return 是否写入
     */
    public static File writeFile(String filePath, String fileName, Long cloudFileSeize, byte[] bytes) {
        return writeFileByByte(filePath, fileName, cloudFileSeize, () -> bytes);
    }

    /**
     * @param filePath       文件路径
     * @param fileName       文件名称
     * @param cloudFileSeize 文件大小
     * @param getBytes       获取文件输入流函数
     * @return 是否写入
     */
    public static File writeFile(String filePath, String fileName, Long cloudFileSeize, Supplier<InputStream> getBytes) {
        return writeFileByByte(filePath, fileName, cloudFileSeize, () -> {
            try {
                return IOUtils.toByteArray(getBytes.get());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * @param filePath       文件路径
     * @param fileName       文件名称
     * @param cloudFileSeize 文件大小
     * @param getBytes       获取文件字节数据函数
     * @return 是否写入
     */
    public static File writeFileByByte(String filePath, String fileName, Long cloudFileSeize, Supplier<byte[]> getBytes) {
        File file = new File(filePath + "/" + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            // 文件未修改,不需要重新落盘
            if (!file.exists() || !cloudFileSeize.equals(FileUtils.sizeOf(file))) {
                FileUtils.writeByteArrayToFile(file, getBytes.get(), false);
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
