package com.springroll.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by anishjoseph on 08/12/16.
 */
public class PropertiesUtil extends PropertyPlaceholderConfigurer {
    private static Map<String, String> uiPropertiesMap = new HashMap();;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory,
                                     Properties props) throws BeansException {
        super.processProperties(beanFactory, props);

        props.keySet().stream().filter(key -> ((String)key).startsWith("ui.")).forEach(key -> uiPropertiesMap.put((String)key, props.getProperty((String)key)));
    }

    public Map<String, String> getUIProperties() {
        return uiPropertiesMap;
    }
}