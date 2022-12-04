package org.openstack4j.core.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Contains an open SSL Context which allows self signed certs and an open hostname verifier
 *
 * @author Jeremy Unruh
 */
public class UntrustedSSL {

    private static final UntrustedSSL INSTANCE = new UntrustedSSL();
    private static final Logger LOG = LoggerFactory.getLogger(UntrustedSSL.class);

    private SSLContext context;
    private HostnameVerifier verifier;
    private X509TrustManager trustManager;

    private UntrustedSSL() {
        try {
            trustManager = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            };
            SSLContext ssc = SSLContext.getInstance("TLS");
            ssc.init(null, new TrustManager[]{trustManager}, new SecureRandom());

            this.context = ssc;
            this.verifier = (s, session) -> true;
        } catch (Throwable t) {
            LOG.error(t.getMessage(), t);
        }
    }

    public static SSLContext getSSLContext() {
        return INSTANCE.context;
    }

    public static X509TrustManager getTrustManager() {
        return INSTANCE.trustManager;
    }

    public static HostnameVerifier getHostnameVerifier() {
        return INSTANCE.verifier;
    }

}
