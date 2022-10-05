package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.platform.vmware.ClsApiClient;
import com.fit2cloud.common.platform.vmware.SslUtil;
import com.fit2cloud.common.platform.vmware.VapiAuthenticationHelper;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.impl.vsphere.entity.VsphereTemplate;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmBaseRequest;
import com.vmware.content.library.Item;
import com.vmware.content.library.ItemModel;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.protocol.HttpConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/22 2:40 PM
 */
public class ContentLibraryUtil {

    private static Logger logger = LoggerFactory.getLogger(ContentLibraryUtil.class);

    public List<F2CImage> getImages(VsphereVmBaseRequest req) {
        VsphereCredential params = req.getVsphereCredential();
        String server = params.getVCenterIp();
        String userName = params.getVUserName();
        String password = params.getVPassword();
        VapiAuthenticationHelper authHelper = new VapiAuthenticationHelper();
        ArrayList<F2CImage> images = new ArrayList<>();
        try {
            VsphereVmClient vsphereClient = req.getVsphereVmClient();
            logger.info("start to login content library");
            StubConfiguration stubConfiguration = authHelper.loginByUsernameAndPassword(server, userName, password, buildHttpConfiguration());
            logger.info("end login content library");
            ClsApiClient client = new ClsApiClient(authHelper.getStubFactory(), stubConfiguration);
            List<String> libs = client.libraryService().list();
            if (libs.size() <= 0) {
                return null;
            }
            Item itemService = client.itemService();
            for (String lib : libs) {
                List<String> items = itemService.list(lib);
                if (items.size() <= 0) {
                    return null;
                }
                for (String itemId : items) {
                    ItemModel item = itemService.get(itemId);
                    if ("ovf".equalsIgnoreCase(item.getType()) || "vm-template".equalsIgnoreCase(item.getType())) {
                        String imageId = item.getLibraryId() + VsphereTemplate.SEPARATOR + item.getId();
                        String imageName = item.getName() + "[内容库]";

                        F2CImage image = new F2CImage(imageId, imageName, item.getDescription(), null, "ContentLibraries", null, null);
                        image.setDiskSize(VsphereUtil.getTemplateDiskSizeInGB(vsphereClient, item.getName()));
                        images.add(image);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            authHelper.logout();
        }
        return images;
    }

    public HttpConfiguration buildHttpConfiguration() {
        return (new HttpConfiguration.Builder()).setSslConfiguration(buildSslConfiguration()).getConfig();
    }

    private HttpConfiguration.SslConfiguration buildSslConfiguration() {
        HttpConfiguration.SslConfiguration sslConfig;
        SslUtil.trustAllHttpsCertificates();
        sslConfig = (new HttpConfiguration.SslConfiguration.Builder()).disableCertificateValidation().disableHostnameVerification().getConfig();
        return sslConfig;
    }

}
