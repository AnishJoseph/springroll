package com.springroll.mdm;

import com.springroll.core.Lov;
import com.springroll.core.exceptions.SpringrollException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 04/11/16.
 */
@Configurable public class NamedQueryLovSource implements ILovSource{
    private static final Logger logger = LoggerFactory.getLogger(NamedQueryLovSource.class);
    private String name;
    private String source;
    @PersistenceContext EntityManager em;


    public String getName() {
        return name;
    }

    @Override
    public List<Lov> getLovs() {
        try {
            List<Lov> lovs = new ArrayList<>();
            Query query = em.createNamedQuery(source);
            List resultList = query.getResultList();
            //FIXME - handle case where both name and value are returned??
            for (Object o : resultList) {
                if(o == null)continue;
                lovs.add(new Lov(o));
            }
            return lovs;
        }catch (Exception e ){
            logger.error("Exception while running named query {} to get the LOVs for LOV source {}. Exception is - {}", source, name, e.getMessage());
            throw new SpringrollException( "lov.source.namedQuery", source, name, e.getMessage());
        }    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
