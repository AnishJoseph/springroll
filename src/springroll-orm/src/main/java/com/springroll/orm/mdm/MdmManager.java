package com.springroll.orm.mdm;

import com.springroll.core.ILovProvider;
import com.springroll.core.Lov;
import com.springroll.core.exceptions.FixableException;
import flexjson.JSONDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 04/11/16.
 */
@Service public class MdmManager {
    private static final Logger logger = LoggerFactory.getLogger(MdmManager.class);
    private MdmDefinitions mdmDefinitions;
    @PersistenceContext EntityManager em;
    @Autowired private ApplicationContext applicationContext;

    @PostConstruct public void init(){
        try {
            Resource resource = new ClassPathResource("mdm.definitions.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            mdmDefinitions = (MdmDefinitions) new JSONDeserializer().deserialize(br, MdmDefinitions.class);
            for (MdmDefinition mdmDefinition : mdmDefinitions.getMasters()) {
                mdmDefinition.getColDefs().add(0, new ColDef("id", false, "num", null));
                for (ColDef colDef : mdmDefinition.getColDefs()) {
                    if("boolean".equals(colDef.getType())){
                        colDef.setLovList(makeBooleanLov());
                        colDef.setDefaultValue(colDef.getDefVal() == null || colDef.getDefVal().equals("false")? false : true);
                    }
                }
            }

        }catch (IOException e){
            logger.error("Error while reading MDM definitions. MDM will be disabled. Error is {} ", e.getMessage());
        }
    }

    private List<Lov> makeBooleanLov(){
        List<Lov> colDefs = new ArrayList<>();
        colDefs.add(new Lov(true, "True"));
        colDefs.add(new Lov(false, "False"));
        return colDefs;
    }

    public MdmData getData(String master){
        if(mdmDefinitions == null){
            logger.debug("MdmDefinitions is empty");
            throw new FixableException("MdmDefinitions is empty");
        }
        MdmDefinition mdmDefinition = null;
        for (MdmDefinition m : mdmDefinitions.getMasters()) {
            if(m.getMaster().equals(master)){
                mdmDefinition = m;
                break;
            }
        }
        if(mdmDefinition == null){
            logger.error("Unable to find MdmDefinition for Master {}", master);
            throw new FixableException("Unable to find MdmDefinition for Master " + master, "mdm.missingdefinition", master);
        }
        MdmData mdmData = new MdmData();
        Query query = em.createNamedQuery(mdmDefinition.getDataQuery());
        query.getResultList();
        mdmData.setData(query.getResultList());
        List<ColDef> colDefs = new ArrayList<>(mdmDefinition.getColDefs().size());
        for (ColDef colDef : mdmDefinition.getColDefs()) {
            ColDef c = new ColDef(colDef.getName(), colDef.isWriteable(), colDef.getType(), colDef.getDefaultValue());
            if(colDef.getType().equals("boolean")){
                c.setLovList(colDef.getLovList());
            } else {
                if(colDef.getLovSource() != null)c.setLovList(getLovs(mdmDefinitions.getLovSource(colDef.getLovSource())));
            }
            colDefs.add(c);
        }

        mdmData.setColDefs(colDefs);
        return mdmData;
    }

    public List<String> getMdmMasterNames(){
        return mdmDefinitions.getMasters().stream().map(MdmDefinition::getMaster).collect(Collectors.toList());
    }
    public List<Lov> getLovs(LovSource lovSource) {
        if("java".equals(lovSource.getType())){
            try {
                ILovProvider lovProvider = (ILovProvider) applicationContext.getBean(lovSource.getSource());
                return lovProvider.getLovs();
            }catch (Exception e ){
                logger.error("Unable to find Spring Bean  with name {}. This was configured as a LOV source in {}", lovSource.getSource(), lovSource.getName());
                throw new FixableException("", "lov.source.missingbean", lovSource.getSource(), lovSource.getName());
            }
        } else if("namedQuery".equals(lovSource.getType())){
            try {
                List<Lov> lovs = new ArrayList<>();
                Query query = em.createNamedQuery(lovSource.getSource());
                List resultList = query.getResultList();
                //FIXME - handle case where both name and value are returned??
                for (Object o : resultList) {
                    lovs.add(new Lov(o));
                }
                return lovs;
            }catch (Exception e ){
                logger.error("Exception while running named query {} to get the LOVs for LOV source {}. Exception is - {}", lovSource.getSource(), lovSource.getName(), e.getMessage());
                throw new FixableException("", "lov.source.namedQuery", lovSource.getSource(), lovSource.getName(), e.getMessage());
            }
        } else if("enum".equals(lovSource.getType())){
            try {
                Class clazz = Class.forName(lovSource.getSource());
                List<Lov> lovs = new ArrayList<>();
                for (Object o : clazz.getEnumConstants()) {
                    lovs.add(new Lov(o));
                }
                return lovs;
            }catch (Exception e ){
                logger.error("Exception while getting Enum Values from class '{}' to get the LOVs. LOV source {}. Exception is - {}", lovSource.getSource(), lovSource.getName(), e.getMessage());
                throw new FixableException("", "lov.source.missingenum", lovSource.getSource(), lovSource.getName(), e.getMessage());
            }
        }
        return null;
    }

}
