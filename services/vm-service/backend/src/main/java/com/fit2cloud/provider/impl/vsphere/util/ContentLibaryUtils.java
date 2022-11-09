package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.common.platform.vmware.SslUtil;
import com.vmware.vapi.protocol.HttpConfiguration;

public class ContentLibaryUtils {

    public static HttpConfiguration buildHttpConfiguration() {
        return (new HttpConfiguration.Builder()).setSslConfiguration(buildSslConfiguration()).getConfig();
    }

    private static HttpConfiguration.SslConfiguration buildSslConfiguration() {
        HttpConfiguration.SslConfiguration sslConfig;
        SslUtil.trustAllHttpsCertificates();
        sslConfig = (new HttpConfiguration.SslConfiguration.Builder()).disableCertificateValidation().disableHostnameVerification().getConfig();
        return sslConfig;
    }

}
