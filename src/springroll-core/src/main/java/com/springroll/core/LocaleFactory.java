package com.springroll.core;

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
    private static boolean cache = true;
    private static SpringrollResourceMessageBundleSource uiMessages;
    private static SpringrollResourceMessageBundleSource serverMessages;

    static {
        serverMessages = new SpringrollResourceMessageBundleSource();
        serverMessages.setBasename("server.messages");
        serverMessages.setCacheSeconds(cache ? -1 : 5);

        uiMessages = new SpringrollResourceMessageBundleSource();
        uiMessages.setBasename("ui.messages");
        uiMessages.setCacheSeconds(cache ? -1 : 5);
    }
    private Map<String, String> resourceBundleToMap(SpringrollResourceMessageBundleSource bundleSource, Locale locale) {
        if(!cache)bundleSource.clearCache();
        Map<String, String> map = new HashMap();

        Properties properties = bundleSource.getMessages(locale);
        for(Map.Entry<Object,Object> entry: properties.entrySet()){
            if(entry.getKey() != null && entry.getValue() != null) {
                map.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return map;
    }
    public Map<String, String> getUIMessagesAsMap(Locale locale) {
        return resourceBundleToMap(uiMessages, locale);
    }

    public String getUILocaleMessage(String message, String defaultMessage, Locale locale) {
        try {
            return uiMessages.getMessage(message, null, locale);
        }catch (Exception e){
            return defaultMessage != null? defaultMessage: message;
        }
    }
    public static String  getLocalizedServerMessage(Locale locale, String messageKey, Object... args) {
        String localizedMessage = serverMessages.getMessage(messageKey, args, locale);
        return localizedMessage;
    }
}
