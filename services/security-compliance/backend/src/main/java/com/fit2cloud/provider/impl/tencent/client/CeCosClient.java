package com.fit2cloud.provider.impl.tencent.client;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSCredentialsProvider;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.auth.COSStaticCredentialsProvider;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.*;
import com.qcloud.cos.internal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/9  11:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CeCosClient extends COSClient {
    private static final Logger log = LoggerFactory.getLogger(COSClient.class);

    private final COSCredentialsProvider credProvider;
    protected ClientConfig clientConfig;
    private final CosHttpClient cosHttpClient;


    public CeCosClient(COSCredentials cred, ClientConfig clientConfig) {
        this((new COSStaticCredentialsProvider(cred)), clientConfig);
    }

    public CeCosClient(COSCredentialsProvider credProvider, ClientConfig clientConfig) {
        super(credProvider, clientConfig);
        this.credProvider = credProvider;
        this.clientConfig = clientConfig;
        this.cosHttpClient = new DefaultCosHttpClient(clientConfig);
    }

    public <X extends CosServiceRequest> CosHttpRequest<X> createRequest(String bucketName, String key, X originalRequest, HttpMethodName httpMethod) {
        return super.createRequest(bucketName, key, originalRequest, httpMethod);
    }


    public <X, Y extends CosServiceRequest> X invoke(CosHttpRequest<Y> request, HttpResponseHandler<CosServiceResponse<X>> responseHandler) throws CosClientException, CosServiceException {
        COSSigner cosSigner = this.clientConfig.getCosSigner();
        CosServiceRequest cosServiceRequest = request.getOriginalRequest();
        COSCredentials cosCredentials;
        if (cosServiceRequest != null && cosServiceRequest.getCosCredentials() != null) {
            cosCredentials = cosServiceRequest.getCosCredentials();
        } else {
            cosCredentials = this.fetchCredential();
        }

        Date expiredTime = new Date(System.currentTimeMillis() + this.clientConfig.getSignExpired() * 1000L);
        boolean isCIWorkflowRequest = cosServiceRequest instanceof CIWorkflowServiceRequest;
        cosSigner.setCIWorkflowRequest(isCIWorkflowRequest);
        cosSigner.sign(request, cosCredentials, expiredTime);
        return this.cosHttpClient.exeute(request, responseHandler);
    }

    private COSCredentials fetchCredential() throws CosClientException {
        if (this.credProvider == null) {
            throw new CosClientException("credentials Provider is null, you must set legal Credentials info when init cosClient.");
        } else {
            COSCredentials cred = this.credProvider.getCredentials();
            if (cred == null) {
                throw new CosClientException("credentials from Provider is null. please check your credentials provider");
            } else {
                return cred;
            }
        }
    }
}
