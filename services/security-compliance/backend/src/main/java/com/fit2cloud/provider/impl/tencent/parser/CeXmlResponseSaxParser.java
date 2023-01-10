package com.fit2cloud.provider.impl.tencent.parser;

import com.fit2cloud.provider.impl.tencent.entity.response.BucketEncryptionResponse;
import com.qcloud.cos.internal.XmlResponsesSaxParser;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  13:38}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CeXmlResponseSaxParser extends XmlResponsesSaxParser {
    @Override
    public void parseXmlInputStream(DefaultHandler handler, InputStream inputStream) throws IOException {
        super.parseXmlInputStream(handler, inputStream);
    }

    public BucketEncryptionResponse parseBucketEncryptionResponse(InputStream inputStream) throws IOException {
        BucketEncryptionResponse handler = new BucketEncryptionResponse();
        parseXmlInputStream(handler, this.sanitizeXmlDocument(handler, inputStream));
        return handler;
    }
}
