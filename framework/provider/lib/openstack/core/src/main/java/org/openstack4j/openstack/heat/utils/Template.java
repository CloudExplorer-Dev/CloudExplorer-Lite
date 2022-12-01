package org.openstack4j.openstack.heat.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Template {

    private static final Logger LOG = LoggerFactory.getLogger(Template.class);
    private final static String GET_FILE = "get_file";
    // template Resource is used for represent the template file or template URL
    private String tplContent;
    private Map<String, String> files = new HashMap<String, String>();
    private URL baseUrl;

    public Template(URL templateRes) throws JsonParseException, IOException {
        setTplContent(Resources.toString(templateRes, Charsets.UTF_8));
        baseUrl = TemplateUtils.baseUrl(templateRes.toString());
        getFileContents();
    }

    public Template(String templateLoc)
            throws JsonParseException, MalformedURLException,
            UnsupportedEncodingException, IOException, URISyntaxException {
        this(TemplateUtils.normaliseFilePathToUrl(templateLoc));
    }

    /*
     * Processing the template file to find the "get_file" tag
     * Save the file name(absolute path) with file content in the files map
     */
    private void getFileContents() {
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, Object> content = (Map<String, Object>) yaml.load(getTplContent());
        try {
            resolveTemplateGetFiles(content);
            resolveTemplateType(content);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void resolveTemplateType(Map<?, ?> map) throws MalformedURLException, IOException {
        for (Object key : map.keySet()) {
            // Ignore if the key is not string. Actually not happening
            if (!(key instanceof String)) {
                continue;
            }

            String skey = (String) key;
            Object value = map.get(skey);

            if (value instanceof String) {
                String valueInString = (String) value;
                //Processing the nested template
                if (isTemplate(skey, valueInString)) {
                    try {
                        final String templateName = valueInString;
                        final URL fullTemplateName = TemplateUtils.normaliseFilePathToUrl(baseUrl.toString(), templateName);

                        if (!files.containsKey(templateName)) {
                            final Template tpl = new Template(fullTemplateName);
                            files.put(templateName, tpl.getTplContent());
                            files.putAll(tpl.getFiles());
                        }
                    } catch (URISyntaxException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }

            if (value instanceof Map<?, ?>) {
                resolveTemplateType((Map<?, ?>) value);
            } else if (value instanceof List<?>) {
                for (Object item : (List<?>) value) {
                    if (item instanceof Map<?, ?>) {
                        resolveTemplateType((Map<?, ?>) item);
                    }
                }
            }
        }
    }

    private void resolveTemplateGetFiles(Map<?, ?> map) throws IOException {
        for (Object key : map.keySet()) {
            // Ignore if the key is not string. Actually not happening
            if (!(key instanceof String)) {
                continue;
            }

            String skey = (String) key;
            Object value = map.get(skey);

            if (isGetFile(skey)) {
                //if key="get_file", the value is the filename
                addToFiles((String) value);
                continue;
            }

            Object subMap = map.get(skey);
            if (subMap instanceof Map<?, ?>) {
                resolveTemplateGetFiles((Map<?, ?>) subMap);
            } else if (subMap instanceof List<?>) {
                for (Object item : (List<?>) subMap) {
                    if (item instanceof Map<?, ?>) {
                        resolveTemplateGetFiles((Map<?, ?>) item);
                    }
                }
            }
        }
    }

    private void addToFiles(String filename) throws IOException {
        if (!files.containsKey(filename)) {
            if (filename.startsWith("/")) {
                files.put(filename, TemplateUtils.readToString(filename));
            } else {
                files.put(filename, TemplateUtils.readToString(baseUrl + filename));
            }
        }
    }

    private boolean isGetFile(String tag) {
        return tag.equals(GET_FILE);
    }

    private boolean isTemplate(String key, String value) {
        if (!key.equals("type")) {
            return false;
        }
        if (value.endsWith(".yaml") || value.endsWith(".template")) {
            return true;
        } else {
            return false;
        }
    }

    public String getTplContent() {
        return tplContent;
    }

    public void setTplContent(String tplContent) {
        this.tplContent = tplContent;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
}
