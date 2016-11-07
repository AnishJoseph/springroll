package com.springroll.mdm;

import com.springroll.core.Lov;
import com.springroll.core.exceptions.FixableException;
import com.springroll.orm.mdm.*;
import com.springroll.router.SpringrollEndPoint;
import com.springroll.router.dto.MdmDTO;
import com.springroll.router.notification.MdmEvent;
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
@Service public class MdmManager extends SpringrollEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(MdmManager.class);
    private MdmDefinitions mdmDefinitions;
    @PersistenceContext EntityManager em;
    @Autowired private ApplicationContext applicationContext;

    private Object convertValue(MdmDefinition mdmDefinition, String value, String colName){
        ColDef colDef = mdmDefinition.getColDefByName(colName);
        //FIXME - handle NOT FOUND
        if(colDef.getType().equals("int"))return Integer.parseInt(value);
        if(colDef.getType().equals("num"))return Double.parseDouble(value);
        if(colDef.getLovSource() != null && mdmDefinitions.getLovSource(colDef.getLovSource()) instanceof EnumLovSource){
            EnumLovSource source = (EnumLovSource) mdmDefinitions.getLovSource(colDef.getLovSource());
            return source.getEnum(value);
        }
        return value;

    }
    public void on(MdmEvent mdmEvent) {
        MdmDTO mdmDTO = mdmEvent.getPayload();
        MdmDefinition mdmDefinition = getDefinitionForMaster(mdmDTO.getMaster());
        try {
            for (MdmChangedRecord mdmChangedRecord : mdmDTO.getChangedRecords()) {
                StringBuffer sb = new StringBuffer("Update " + mdmDTO.getMaster() + " o SET ");
                String prepend = "";
                for (MdmChangedColumn mdmChangedColumn : mdmChangedRecord.getMdmChangedColumns()) {
                    sb.append(prepend + "o." + mdmChangedColumn.getColName() + " = :" + mdmChangedColumn.getColName());
                    prepend = ", ";
                }
                sb.append(" WHERE o.id = :id");
                Query query = em.createQuery(sb.toString());
                query.setParameter("id", mdmChangedRecord.getId());
                for (MdmChangedColumn mdmChangedColumn : mdmChangedRecord.getMdmChangedColumns()) {
                    query.setParameter(mdmChangedColumn.getColName(), convertValue(mdmDefinition, (String)mdmChangedColumn.getVal(), mdmChangedColumn.getColName()));
                }
                int rowsChanged = query.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
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
                    } else if("int".equals(colDef.getType())){
                        colDef.setDefaultValue(colDef.getDefVal() == null ? null : Integer.parseInt(colDef.getDefVal()));
                    } else if("num".equals(colDef.getType())){
                        colDef.setDefaultValue(colDef.getDefVal() == null ? null : Float.parseFloat(colDef.getDefVal()));
                    }
                }
            }
            mdmDefinitions.init();

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

    private MdmDefinition getDefinitionForMaster(String master){
        for (MdmDefinition m : mdmDefinitions.getMasters()) {
            if(m.getMaster().equals(master)){
                return m;
            }
        }
        return null;
    }
    public MdmData getData(String master){
        if(mdmDefinitions == null){
            logger.debug("MdmDefinitions is empty");
            throw new FixableException("MdmDefinitions is empty");
        }
        MdmDefinition mdmDefinition = getDefinitionForMaster(master);
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
                if(colDef.getLovSource() != null)c.setLovList(mdmDefinitions.getLovSource(colDef.getLovSource()).getLovs());
            }
            colDefs.add(c);
        }

        mdmData.setColDefs(colDefs);
        return mdmData;
    }

    public List<String> getMdmMasterNames(){
        return mdmDefinitions.getMasters().stream().map(MdmDefinition::getMaster).collect(Collectors.toList());
    }
}
