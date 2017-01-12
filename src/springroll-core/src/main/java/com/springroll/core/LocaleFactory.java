package com.springroll.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

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

    static {
        coreMessages = new SpringrollResourceMessageBundleSource();
        coreMessages.setBasename("core.springroll.messages");
        coreMessages.setCacheSeconds(cache ? -1 : 5);
    }

    public Map<String, String> getUIMessagesAsMap(Locale locale) {
        if(!cache) coreMessages.clearCache();
        Map<String, String> map = new HashMap();

        Properties properties = coreMessages.getMessages(locale);
        for(Map.Entry<Object,Object> entry: properties.entrySet()){
            if(entry.getKey() != null && entry.getValue() != null && entry.getKey().toString().startsWith("ui.")) {
                map.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return map;
    }

    public static String getLocalizedMessage(Locale locale, String messageKey, Object... args) {
        try {
            return coreMessages.getMessage(messageKey, args, locale);
        }catch (NoSuchMessageException exception){
            logger.info("Message key '{}' has no entry in the resource bundle", messageKey);
            return messageKey;
        }
    }
}
