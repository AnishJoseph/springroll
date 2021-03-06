package com.springroll.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Created by anishjoseph on 17/10/16.
 */
@Service
public class LocaleFactory {
    private static final Logger logger = LoggerFactory.getLogger(LocaleFactory.class);
    private static boolean cache = false;
    private static SpringrollResourceMessageBundleSource coreMessages;
    private static SpringrollResourceMessageBundleSource applicationMessages = null;

    static {
        coreMessages = new SpringrollResourceMessageBundleSource();
        coreMessages.setBasename("core.springroll.messages");
        coreMessages.setCacheSeconds(cache ? -1 : 5);

        applicationMessages = new SpringrollResourceMessageBundleSource();
        applicationMessages.setBasename("sr.application.messages");
        applicationMessages.setCacheSeconds(cache ? -1 : 5);
    }

    /**
     * Extracts all property keys that start with 'ui.'
     * @param properties
     * @return - a map of the property key and values that start with 'ui.'
     */
    private Map<String, String> getUIProperties(Properties properties){
        Map<String, String> map = new HashMap();
        for(Map.Entry<Object,Object> entry: properties.entrySet()){
            if(entry.getKey() != null && entry.getValue() != null && entry.getKey().toString().startsWith("ui.")) {
                map.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return map;

    }
    public Map<String, String> getUIMessagesAsMap(Locale locale) {
        if(!cache) {
            coreMessages.clearCache();
            if(applicationMessages != null)applicationMessages.clearCache();
        }
        Map<String, String> map = getUIProperties(coreMessages.getMessages(locale));

        if(applicationMessages != null) {
            map.putAll(getUIProperties(applicationMessages.getMessages(locale)));
        }
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);
        map.put("decimalSeparator", decimalFormatSymbols.getDecimalSeparator()+"");
        map.put("groupingSeparator", decimalFormatSymbols.getGroupingSeparator()+"");
        return map;
    }

    public static String getLocalizedMessage(Locale locale, String messageKey, Object... args) {
        try {
            return coreMessages.getMessage(messageKey, args, locale);
        } catch (NoSuchMessageException exception){
            try {
                return applicationMessages.getMessage(messageKey, args, locale);
            } catch (NoSuchMessageException e) {
                logger.info("Message key '{}' has no entry in the resource bundle", messageKey);
                return messageKey;
            }
        }
    }
}
