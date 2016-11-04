package com.springroll.reporting.grid;

import com.springroll.core.exceptions.FixableException;
import com.springroll.core.Lov;
import com.springroll.reporting.ReportParameter;
import flexjson.JSONDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 18/10/16.
 */
@Component public class GridReporter {
    private static final Logger logger = LoggerFactory.getLogger(GridReporter.class);
    @PersistenceContext EntityManager em;
    private GridConfiguration gridConfiguration;

    @PostConstruct public void init(){
        try {
            Resource resource = new ClassPathResource("grid.definitions.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            gridConfiguration = (GridConfiguration) new JSONDeserializer().deserialize(br, GridConfiguration.class);
            System.out.println("");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public GridReport getGrid(String gridName, Map<String, Object> parameters) {
        Grid grid = gridConfiguration.findGridByName(gridName);
        if(grid == null){
            logger.error("Unable to find grid by name '{}", gridName);
            throw new RuntimeException("Unable to find grid by name " + gridName); //FIXME - handle correctly
        }

        List data = executeQuery(grid.getNamedQuery(), parameters);
        GridReport gridReport = new GridReport();
        gridReport.setColumns(grid.getColumns());
        gridReport.setData(data);
        return gridReport;
    }

    public List<ReportParameter> getParameters(String gridName, Map<String, Object> parameters){
        List<ReportParameter> reportParameters = new ArrayList<>();
        Grid grid = gridConfiguration.findGridByName(gridName);
        if(grid == null){
            logger.error("Unable to find grid by name '{}", gridName);
            throw new RuntimeException("Unable to find grid by name " + gridName); //FIXME - handle correctly
        }
        Query query = em.createNamedQuery(grid.getNamedQuery());
        for (Parameter<?> parameter : query.getParameters()) {
            GridParameter gridParameter = gridConfiguration.findParameterByName(parameter.getName());
            boolean multiSelect = gridParameter == null ? false: gridParameter.isMultiSelect();
            String displayName = gridParameter == null || gridParameter.getDisplayName() == null? parameter.getName(): gridParameter.getDisplayName();
            List<Lov> lovs;
            if(gridParameter != null && gridParameter.getNamedQuery() != null) {
                lovs = getLovsFromNamedQuery(gridParameter.getNamedQuery(), parameters);
            } else if(gridParameter != null && gridParameter.getList() != null){
                lovs = getLovsFromList(gridParameter.getList());
            } else {
                lovs = getLovsFromEnum(parameter.getParameterType());
            }
            reportParameters.add(new ReportParameter(parameter.getName(), displayName, parameter.getParameterType().getName(), true, true, multiSelect, true, lovs ));
        }
        return reportParameters;
    }

    private List<Lov> getLovsFromList(String list) {
        List<Lov> lovs = new ArrayList<>();
        String[] split = list.split(":");
        for (String s : split) {
            lovs.add(new Lov(s));
        }
        return lovs;
    }
//    public ReportParameter(String name, String javaType, boolean isConstrained, boolean mandatory, boolean multiSelect, boolean visible) {

    private List<Lov> getLovsFromEnum(Class enumClass){
        List<Lov> lovs = new ArrayList<>();
        Object[] enumConstants = enumClass.getEnumConstants();
        if(enumConstants == null)return null;
        for (Object enumConstant : enumConstants) {
            lovs.add(new Lov(enumConstant.toString()));
        }
        return lovs;
    }

    private List<Lov> getLovsFromNamedQuery(String queryName, Map<String, Object> parameters){
        List<Lov> lovs = new ArrayList<>();
        List list = executeQuery(queryName, parameters);
        for (Object o : list) {
            lovs.add(new Lov(o.toString()));
        }
        return lovs;
    }

    private List executeQuery(String namedQuery, Map<String, Object> parameters){
        Query query = em.createNamedQuery(namedQuery);
        for (Parameter<?> parameter : query.getParameters()) {
            Object paramValue = parameters.get(parameter.getName());
            if(paramValue == null){
                logger.error("The named query '{}' is missing a parameter called '{}'", namedQuery, parameter.getName());
                throw new FixableException("Missing parameter " + parameter.getName() + " in named query " + namedQuery, "missing.namedquery.parameter", parameter.getName());
            }
            try {
                Object o = convert(paramValue, parameter);
                query.setParameter(parameter.getName(), o);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("xx");//FIXME
            }
        }
        return query.getResultList();
    }

    private Object convert(Object paramValue, Parameter parameter){
        if(parameter.getParameterType().equals(LocalDateTime.class)){
            String x = (String) paramValue;
            x = x + " 00:00";
            //FIXME - handle this correctly
            return LocalDateTime.parse(x, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        if(parameter.getParameterType().equals(Boolean.class)){
            return "true".equalsIgnoreCase((String)paramValue);
        }
        return paramValue;
    }
}
