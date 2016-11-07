package com.springroll.orm.mdm;

import com.springroll.core.exceptions.FixableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class EnumLovSource implements ILovSource {
    private static final Logger logger = LoggerFactory.getLogger(EnumLovSource.class);
    private String name;
    private String source;
    private Class enumClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        try {
            enumClass = Class.forName(source);
        } catch (ClassNotFoundException e) {
            logger.error("Exception while getting Enum Values from class '{}' to get the LOVs. LOV source {}. Exception is - {}", source, name, e.getMessage());
            throw new FixableException("", "lov.source.missingenum", source, name, e.getMessage());
        }

    }

    public Class getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class enumClass) {
        this.enumClass = enumClass;
    }
}
