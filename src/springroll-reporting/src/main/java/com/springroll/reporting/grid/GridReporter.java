package com.springroll.reporting.grid;

import com.springroll.reporting.Lov;
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
import java.util.ArrayList;
import java.util.HashMap;
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

    public List<ReportParameter> getParameters(String gridName, Map<String, Object> parameters){
        List<ReportParameter> reportParameters = new ArrayList<>();
        Grid grid = gridConfiguration.findGridByName(gridName);
        if(grid == null){
            logger.error("Unable to find grid by name '{}", gridName);
            throw new RuntimeException("Unable to find grid by name " + gridName); //FIXME - handle correctly
        }
        Query query = em.createNamedQuery(grid.getNamedQuery());
        for (Parameter<?> parameter : query.getParameters()) {
            /* parameter naming convention
                if the parameter is used in an in condition - i.e. multiselect is true -  param name should end with '__list'
                if the lov for the param comes from a named sql the param name should start with nq__<name of named query>__
                examples
                1 - for a param whose lovs' come from a named query (called nq1) and the user is allowed to multiselect then
                    the name of the parameter will be nq__nq1__paramname.list
                2 - for a param whose lovs' come from a named query (called nq1) and the user is NOT allowed to multiselect then
                    the name of the parameter will be nq__nq1__paramname
                3 - for a param whose lovs' come from a enum and the user is NOT allowed to multiselect then the name of the parameter can be anything
                4 - for a param whose lovs' come from a enum and the user is allowed to multiselect then the name of the parameter paramname__list
              */
            boolean multiSelect = parameter.getName().endsWith("__in");
            List<Lov> lovs;
            if(parameter.getName().startsWith("nq__")){
                lovs = getLovsFromNamedQuery(parameter.getName(), parameters);
            } else {
                lovs = getLovsFromEnum(parameter.getParameterType());
            }
            reportParameters.add(new ReportParameter(parameter.getName(), parameter.getParameterType().getName(), true, true, multiSelect, true, lovs ));
        }
        return reportParameters;
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

    private List<Lov> getLovsFromNamedQuery(String parameterName, Map<String, Object> parameters){
        List<Lov> lovs = new ArrayList<>();
        String[] split = parameterName.split("__");
        List list = executeQuery(split[1], parameters);
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
                throw new RuntimeException("Missing parameter " + parameter.getName() + " in named query " + namedQuery);
            }
            query.setParameter(parameter.getName(), paramValue);
        }
        return query.getResultList();
    }


    public void test(){
        try {

            Map<String, Object> params = new HashMap<>();
            List<ReportParameter> p = getParameters("TestGrid1", params);
            System.out.println(p.size());
            boolean b = true;
            if(b)return;


            params.put("userId", "ANISH");
            List data = executeQuery("singleStringParam", params);
            System.out.println(data.size());

            params.clear();
            List<String> users = new ArrayList<>();
            users.add("ANISH");
            users.add("BOM1");
            params.put("userIds", users);
            data = executeQuery("StringListParam", params);
            System.out.println(data.size());

            params.put("completeStatus", new Boolean(true));
            data = executeQuery("StringListBooleanParam", params);
            System.out.println(data.size());

            params.put("date", LocalDateTime.now());
            data = executeQuery("StringListBooleanDateParam", params);
            System.out.println(data.size());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
