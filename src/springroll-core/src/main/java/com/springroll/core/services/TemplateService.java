package com.springroll.core.services;

import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 13/10/16.
 */
public interface TemplateService {
    Map<String,String> getTemplates(List<String> templateIds);
}
