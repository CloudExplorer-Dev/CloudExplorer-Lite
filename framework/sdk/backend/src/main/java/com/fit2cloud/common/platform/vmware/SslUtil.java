package com.fit2cloud.common.platform.vmware;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SslUtil {
    public SslUtil() {
    }

    public static KeyStore createTrustStoreForServer(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException var4) {
            throw new IllegalArgumentException(var4);
        }

        String host = uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            port = 443;
        }

        return createTrustStoreForServer(host, port);
    }

    public static KeyStore createTrustStoreForServer(String host, int port) {
        TrustManager trustAll = new X509TrustManager() {
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init((KeyManager[])null, new TrustManager[]{trustAll}, (SecureRandom)null);
            SSLSocket s = (SSLSocket)ctx.getSocketFactory().createSocket(host, port);
            Certificate[] chain = s.getSession().getPeerCertificates();
            Certificate rootCert = chain[chain.length - 1];
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load((InputStream)null, (char[])null);
            trustStore.setCertificateEntry(host, rootCert);
            return trustStore;
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }

    public static void trustAllHttpsCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[1];
            TrustManager tm = new TrustAllTrustManager();
            trustAllCerts[0] = tm;
            SSLContext sc = SSLContext.getInstance("SSL");
            SSLSessionContext sslsc = sc.getServerSessionContext();
            sslsc.setSessionTimeout(0);
            sc.init((KeyManager[])null, trustAllCerts, (SecureRandom)null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (KeyManagementException var5) {
            throw new RuntimeException(var5);
        } catch (NoSuchAlgorithmException var6) {
            throw new RuntimeException(var6);
        } catch (IllegalArgumentException var7) {
            throw new RuntimeException(var7);
        }
    }

    public static X509Certificate loadCertificate(String filePath) throws IOException, CertificateException {
        ByteArrayInputStream bis = new ByteArrayInputStream(Files.readAllBytes(Paths.get(filePath)));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate)cf.generateCertificate(bis);
    }

    public static KeyStore loadTrustStore(String filePath, String password) throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        InputStream truststoreStream = new FileInputStream(filePath);

        KeyStore var4;
        try {
            trustStore.load(truststoreStream, password.toCharArray());
            var4 = trustStore;
        } finally {
            truststoreStream.close();
        }

        return var4;
    }
}
