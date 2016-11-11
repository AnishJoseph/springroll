package com.springroll.mdm;

import com.springroll.core.ILovProvider;
import com.springroll.core.Lov;
import com.springroll.core.exceptions.SpringrollException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by anishjoseph on 04/11/16.
 */
@Configurable public class JavaLovSource implements ILovSource {
    private static final Logger logger = LoggerFactory.getLogger(JavaLovSource.class);
    private String name;
    private String source;
    private ILovProvider provider;
    @Autowired private ApplicationContext applicationContext;


    public String getName() {
        return name;
    }

    @Override
    public List<Lov> getLovs() {
        return provider.getLovs();
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
            provider = (ILovProvider) applicationContext.getBean(source);
        }catch (Exception e ){
            logger.error("Unable to find Spring Bean  with name {}. This was configured as a LOV source in {}", source, name);
            throw new SpringrollException("lov.source.missingbean", source, name);
        }

    }

}
