package com.springroll.api.facade;

import com.springroll.core.services.TemplateService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 13/10/16.
 */
@Component
public class TemplateManager implements TemplateService{
    private static final Logger logger = LoggerFactory.getLogger(TemplateManager.class);

    @Autowired
    private ServletContext context;

    private Map<String, String> templateCache = new HashMap<>();


    private boolean CACHED = false;

    private void loadTemplate(Map<String, String> templates, String templateId) {
        try {
            String template;
            if (CACHED) {
                if (!templateCache.containsKey(templateId)) {
                    template = readFileContentFromDisk(templateId);
                    templateCache.put(templateId, template);
                } else {
                    template = templateCache.get(templateId);
                }
            } else {
                template = readFileContentFromDisk(templateId);
            }

            templates.put(templateId, template);

        } catch (Exception e) {
            logger.error("Error while loading template '{}' - error is '{}'", templateId, e.getMessage());
            return;
        }
    }

    private String readFileContentFromDisk(String templateId) throws IOException {
        URL resource = context.getResource("/templates/" + templateId.substring(1) + ".htm");
        return IOUtils.toString(resource);
    }

    @Override
    public Map<String,String> getTemplates(List<String> templateIds) {
        Map<String, String> templates = new HashMap();

        for (String templateId : templateIds) {
            loadTemplate(templates, templateId);
        }

        return templates;

    }
}
