package com.springroll.mdm;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springroll.core.Lov;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.orm.entities.ReviewStep;
import com.springroll.orm.repositories.Repositories;
import com.springroll.router.SpringrollEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 04/11/16.
 */
@Service public class MdmManager extends SpringrollEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(MdmManager.class);
    private MdmDefinitions mdmDefinitions;
    @PersistenceContext EntityManager em;
    @Autowired private ApplicationContext applicationContext;
    @Autowired Repositories repositories;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //FIXME take this from properties file
    private DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); //FIXME take this from properties file

    @Value("${mdm.showModifedRecords}")
    private boolean showModifedRecords = false;

    public void on(MdmEvent mdmEvent) {
        MdmDTO mdmDTO = mdmEvent.getPayload();
        MdmDefinition mdmDefinition = getDefinitionForMaster(mdmDTO.getMaster());
        try {
            for (MdmChangedRecord mdmChangedRecord : mdmDTO.getChangedRecords()) {
                Object entity = em.find(mdmDefinition.getMasterClass(), mdmChangedRecord.getId());
                BeanWrapper wrapper = new BeanWrapperImpl(entity);
                Map<String, Object> map = new HashMap<>();
                for (String colName : mdmChangedRecord.getMdmChangedColumns().keySet()) {
                    ColDef colDef = mdmDefinition.getColDefByName(colName);
                    if(!colDef.isWriteable())throw new SpringrollException("mdm.notwritable", colDef.getName(), mdmDefinition.getMasterClass().getSimpleName() );
                    MdmChangedColumn mdmChangedColumn = mdmChangedRecord.getMdmChangedColumns().get(colName);
                    map.put(colName, mdmChangedColumn.getVal());
                    if(colDef.getType().equalsIgnoreCase("date")){
                        map.put(colName,LocalDate.parse((String)mdmChangedColumn.getVal(), dateFormatter));
                    }
                    if(colDef.getType().equalsIgnoreCase("datetime")){
                        map.put(colName,LocalDateTime.parse((String)mdmChangedColumn.getVal(), datetimeFormatter));
                    }
                }
                wrapper.setPropertyValues(map);
            }
            for (Map<String, Object> newRecord : mdmDTO.getNewRecords()) {
                newRecord.remove("id");  //ID will be generated by JPA
                for (String colName : newRecord.keySet()) {
                    ColDef colDef = mdmDefinition.getColDefByName(colName);
                    if(colDef.getType().equalsIgnoreCase("date")){
                        newRecord.put(colName,LocalDate.parse((String)newRecord.get(colName), dateFormatter));
                    }
                    else if(colDef.getType().equalsIgnoreCase("datetime")){
                        newRecord.put(colName,LocalDateTime.parse((String)newRecord.get(colName), datetimeFormatter));
                    }
                }
                BeanWrapper wrapper = new BeanWrapperImpl(mdmDefinition.getMasterClass());
                wrapper.setPropertyValues(newRecord);
                Object entity = wrapper.getWrappedInstance();
                em.persist(entity);
            }
            logger.debug("{} row(s) updated and {} row(s) created in Master {} via MDM by {}", mdmDTO.getChangedRecords().size(),mdmDTO.getNewRecords().size(), mdmDTO.getMaster(), SpringrollSecurity.getUser().getUsername());
        }catch (Exception e){
            e.printStackTrace();         //FIXME
        }
    }
    @PostConstruct public void init(){
        try {
            Resource resource = new ClassPathResource("mdm.definitions.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mdmDefinitions = mapper.readValue(br, MdmDefinitions.class);
            for (MdmDefinition mdmDefinition : mdmDefinitions.getMasters()) {
                mdmDefinition.getColDefs().add(0, new ColDef("id", false, "num", null, false));
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
        MdmDefinition mdmDefinition = getMdmDefinition(master);
        MdmData mdmData = new MdmData();
        List<ColDef> colDefs = new ArrayList<>(mdmDefinition.getColDefs().size());
        for (ColDef colDef : mdmDefinition.getColDefs()) {
            ColDef c = new ColDef(colDef);
            if(colDef.getType().equals("boolean")){
                c.setLovList(colDef.getLovList());
            } else {
                if(colDef.getLovSource() != null)c.setLovList(mdmDefinitions.getLovSource(colDef.getLovSource()).getLovs());
            }
            colDefs.add(c);
        }

        mdmData.setColDefs(colDefs);

        /* Now find any MDM records for this master that are review - these includes those that are new and those that were modified */
        List<ReviewStep> recsUnderReview = repositories.reviewStep.findByChannelAndSearchIdAndSerializedEventIsNotNull(mdmDefinitions.getReviewChannelName(), "MDM:" + master);
        List<Long> idsUnderReview = new ArrayList<>();
        for (ReviewStep recUnderReview : recsUnderReview) {
            MdmDTO mdmDTO = (MdmDTO) recUnderReview.getEvent().getPayload();
            idsUnderReview.addAll(mdmDTO.getChangedRecords().stream().map( MdmChangedRecord::getId).collect(Collectors.toList()));
        }

        List<Long> tmpIdsUnderReview = new ArrayList<>();
        if(showModifedRecords)tmpIdsUnderReview.addAll(idsUnderReview);
        tmpIdsUnderReview.add(-1l);
        List<Object[]> resultList = em.createNamedQuery(mdmDefinition.getGetMdmRecords()).setParameter("idsUnderReview", tmpIdsUnderReview).getResultList();

        for (int i = 0; i < colDefs.size(); i++) {
            if(colDefs.get(i).isMultiSelect() && !resultList.isEmpty()){
                //FIXME Handling the fact that we store list as CSV in the DB -
                // example in User we store the List of Roles as a CSV - instead of onetomany, elementcollection etc
                for (Object[] data : resultList) {
                    if(data[i] == null)continue;
                    if(data[i] != null && (data[i] instanceof Collection || data[i].getClass().isArray())){
                        //Data is already in an collection or array - no data massaging to be done.
                        break;
                    }
                    /* Data exists and now we assume its in CSV format */
                    data[i] = ((String)data[i]).split(",");
                }
            }
            if(colDefs.get(i).getType().equalsIgnoreCase("date")){
                for (Object[] data : resultList) {
                    LocalDate date = (LocalDate) data[i];
                    data[i] = date.format(dateFormatter);
                }
            } else if(colDefs.get(i).getType().equalsIgnoreCase("datetime")){
                for (Object[] data : resultList) {
                    LocalDateTime date = (LocalDateTime) data[i];
                    data[i] = date.format(datetimeFormatter);
                }
            }
        }
        int fakeIndex = -1;

        /* Add the ids of all updated records under review (previously computed) into the list of records under review */
        mdmData.getRecIdsUnderReview().addAll(idsUnderReview);

        if(showModifedRecords) {
            for (ReviewStep recUnderReview : recsUnderReview) {
                MdmDTO mdmDTO = (MdmDTO) recUnderReview.getEvent().getPayload();

                /* Add data for all new records */
                for (Map<String, Object> newRecord : mdmDTO.getNewRecords()) {
                    Object[] newRecData = new Object[mdmData.getColDefs().size()];
                    resultList.add(newRecData);
                    mdmData.getRecIdsUnderReview().add((long) fakeIndex);
                    int i = 0;
                    for (ColDef colDef : mdmData.getColDefs()) {
                        newRecData[i++] = colDef.getName().equalsIgnoreCase("id") ? fakeIndex--: newRecord.get(colDef.getName());
                    }
                }

                /*  Get the notification for this review step - the notification has the data for the record under modification - we need
                    this to display the changed record to the user (the record will be disabled but nevertheless needs to be shown
                */
                MdmReviewNotificationMessage mdmReviewNotificationMessage = (MdmReviewNotificationMessage) repositories.notification.findOne(recUnderReview.getNotificationId()).getNotificationMessage();
                for (MdmChangedRecord mdmChangedRecord : mdmReviewNotificationMessage.getMdmChangesForReview().getChangedRecords()) {
                    Object[] newRecData = new Object[mdmData.getColDefs().size()];
                    resultList.add(newRecData);
                    int i = 0;
                    for (ColDef colDef : mdmData.getColDefs()) {
                        newRecData[i++] = mdmChangedRecord.getMdmChangedColumns().get(colDef.getName()).getVal();
//                        if (colDef.getType().equalsIgnoreCase("date")) {
//                            newRecData[i - 1] = ((LocalDate) newRecData[i - 1]).format(formatter);
//                        }
                    }
                }
            }
        }

        mdmData.setData(resultList);
        return mdmData;
    }

    public List<String> getMdmMasterNames(){
        return mdmDefinitions.getMasters().stream().map(MdmDefinition::getMaster).collect(Collectors.toList());
    }

    public List<ColDef> getColDefs(String master){
        return getMdmDefinition(master).getColDefs();
    }

    private MdmDefinition getMdmDefinition(String master){
        if(mdmDefinitions == null){
            logger.debug("MdmDefinitions is empty");
            throw new SpringrollException("MdmDefinitions is empty");
        }
        MdmDefinition mdmDefinition = getDefinitionForMaster(master);
        if(mdmDefinition == null){
            logger.error("Unable to find MdmDefinition for Master {}", master);
            throw new SpringrollException( "mdm.missingdefinition", master);
        }
        return mdmDefinition;
    }

    public List<Object[]> getData(String master, List<Long> changedIds) {
        MdmDefinition mdmDefinition = getMdmDefinition(master);
        Query query = em.createNamedQuery(mdmDefinition.getGetMdmRecordsForIds());
        query.setParameter("ids", changedIds);
        return query.getResultList();
    }
}
