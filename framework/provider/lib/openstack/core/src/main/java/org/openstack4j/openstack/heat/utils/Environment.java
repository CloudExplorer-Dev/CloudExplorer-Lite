package org.openstack4j.openstack.heat.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Environment {

    private String envContent;
    private Map<String, String> files = new HashMap<String, String>();
    private URL baseUrl;

    public Environment(URL environmentRes) throws JsonParseException, IOException, URISyntaxException {
        setEnvContent(Resources.toString(environmentRes, Charsets.UTF_8));
        setBaseUrl(TemplateUtils.baseUrl(environmentRes.toString()));
        getFileContent();
    }

    public Environment(String environmentLoc)
            throws JsonParseException, MalformedURLException,
            UnsupportedEncodingException, IOException, URISyntaxException {
        this(TemplateUtils.normaliseFilePathToUrl(environmentLoc));
    }


    @SuppressWarnings("unchecked")
    private Map<String, String> getResourceRegistry() {
        Yaml yaml = new Yaml();
        Map<String, Object> content = (Map<String, Object>) yaml.load(getEnvContent());
        return (Map<String, String>) content.get("resource_registry");
    }

    /*
     * Processing the template files located in the resource_registry part in the environment file
     */
    private void getFileContent()
            throws JsonParseException, MalformedURLException, UnsupportedEncodingException, IOException, URISyntaxException {
        Map<String, String> rr = getResourceRegistry();
        if (rr == null) return;
        if (rr.get("base_url") != null) setBaseUrl(new URL(rr.get("base_url")));
        for (String resourceType : rr.keySet()) {
            if (resourceType.equals("base_url"))
                continue;

            if ("OS::Heat::None".equals(rr.get(resourceType)))
                continue;

            URL tplUrl = new URL(baseUrl, rr.get(resourceType));

            Template tpl = new Template(tplUrl);
            files.put(rr.get(resourceType), tpl.getTplContent());
            Map<String, String> fileFromTemplate = tpl.getFiles();
            for (String file : fileFromTemplate.keySet()) {
                if (!files.containsKey(file)) {
                    files.put(file, fileFromTemplate.get(file));
                }
            }

        }
    }

    public String getEnvContent() {
        return envContent;
    }

    public void setEnvContent(String envContent) {
        this.envContent = envContent;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public URL getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URL baseUrl) {
        this.baseUrl = baseUrl;
    }
}
