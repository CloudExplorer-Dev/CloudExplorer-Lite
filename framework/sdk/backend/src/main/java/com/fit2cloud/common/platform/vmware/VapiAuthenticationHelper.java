package com.fit2cloud.common.platform.vmware;


import com.vmware.cis.Session;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.bindings.StubFactory;
import com.vmware.vapi.cis.authn.ProtocolFactory;
import com.vmware.vapi.cis.authn.SecurityContextFactory;
import com.vmware.vapi.core.ApiProvider;
import com.vmware.vapi.core.ExecutionContext;
import com.vmware.vapi.protocol.ClientConfiguration;
import com.vmware.vapi.protocol.HttpConfiguration;
import com.vmware.vapi.protocol.ProtocolConnection;
import com.vmware.vapi.saml.SamlToken;
import com.vmware.vapi.security.SessionSecurityContext;

import java.security.PrivateKey;

public class VapiAuthenticationHelper {
    private Session sessionSvc;
    private StubFactory stubFactory;
    public static final String VAPI_PATH = "/api";

    public VapiAuthenticationHelper() {
    }

    public StubConfiguration loginByUsernameAndPassword(String server, String username, String password, HttpConfiguration httpConfig) throws Exception {
        if (this.sessionSvc != null) {
            throw new Exception("Session already created");
        } else {
            this.stubFactory = this.createApiStubFactory(server, httpConfig);
            ExecutionContext.SecurityContext securityContext = SecurityContextFactory.createUserPassSecurityContext(username, password.toCharArray());
            StubConfiguration stubConfig = new StubConfiguration(securityContext);
            Session session = (Session)this.stubFactory.createStub(Session.class, stubConfig);
            char[] sessionId = session.create();
            SessionSecurityContext sessionSecurityContext = new SessionSecurityContext(sessionId);
            stubConfig.setSecurityContext(sessionSecurityContext);
            this.sessionSvc = (Session)this.stubFactory.createStub(Session.class, stubConfig);
            return stubConfig;
        }
    }

    public StubConfiguration loginBySamlBearerToken(String server, SamlToken samlBearerToken, HttpConfiguration httpConfig) throws Exception {
        if (this.sessionSvc != null) {
            throw new Exception("Session already created");
        } else {
            this.stubFactory = this.createApiStubFactory(server, httpConfig);
            ExecutionContext.SecurityContext samlSecurityContext = SecurityContextFactory.createSamlSecurityContext(samlBearerToken, (PrivateKey)null);
            StubConfiguration stubConfig = new StubConfiguration(samlSecurityContext);
            Session session = (Session)this.stubFactory.createStub(Session.class, stubConfig);
            char[] sessionId = session.create();
            SessionSecurityContext sessionSecurityContext = new SessionSecurityContext(sessionId);
            stubConfig.setSecurityContext(sessionSecurityContext);
            this.sessionSvc = (Session)this.stubFactory.createStub(Session.class, stubConfig);
            return stubConfig;
        }
    }

    public void logout() {
        if (this.sessionSvc != null) {
            this.sessionSvc.delete();
        }

    }

    private StubFactory createApiStubFactory(String server, HttpConfiguration httpConfig) throws Exception {
        ProtocolFactory pf = new ProtocolFactory();
        String apiUrl = "https://" + server + "/api";
        ProtocolConnection connection = pf.getHttpConnection(apiUrl, (ClientConfiguration)null, httpConfig);
        ApiProvider provider = connection.getApiProvider();
        StubFactory stubFactory = new StubFactory(provider);
        return stubFactory;
    }

    public StubFactory getStubFactory() {
        return this.stubFactory;
    }
}
