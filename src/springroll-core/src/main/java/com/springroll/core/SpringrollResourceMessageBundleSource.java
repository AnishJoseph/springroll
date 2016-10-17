package com.springroll.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by anishjoseph on 17/10/16.
 */
public class SpringrollResourceMessageBundleSource extends ReloadableResourceBundleMessageSource {

    private static final Logger logger = LoggerFactory.getLogger(SpringrollResourceMessageBundleSource.class);

    @Override
    protected Properties loadProperties(Resource resource, String fileName) throws IOException {

        return super.loadProperties(resource, fileName);
    }

    /**
     * Gets all messages for presented Locale.
     * @param locale user request's locale
     * @return all messages
     */
    public Properties getMessages(Locale locale){
        return getMergedProperties(locale).getProperties();
    }
}
