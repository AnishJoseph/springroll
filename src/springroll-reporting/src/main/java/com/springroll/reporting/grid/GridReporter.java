package com.springroll.reporting.grid;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springroll.core.Lov;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.SpringrollUtils;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.core.services.reporting.GridReportingService;
import com.springroll.core.services.reporting.IGridReport;
import com.springroll.core.services.reporting.IReportParameter;
import com.springroll.reporting.ReportParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 18/10/16.
 */
@Component public class GridReporter implements GridReportingService {
    private static final Logger logger = LoggerFactory.getLogger(GridReporter.class);
    @PersistenceContext EntityManager em;
    private GridConfiguration gridConfiguration;
    @Autowired SpringrollUtils springrollUtils;

    @Autowired private ApplicationContext applicationContext;

    @PostConstruct public void init(){
        try {
            Resource resource = new ClassPathResource("grid.definitions.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            gridConfiguration = mapper.readValue(br, GridConfiguration.class);
            for (NamedQuery namedQuery : gridConfiguration.getNamedQueries()) {
                em.getEntityManagerFactory().addNamedQuery(namedQuery.getName(), em.createQuery(namedQuery.getQuery()));
            }
            for (Grid grid : gridConfiguration.getGrids()) {
                for (GridColumn gridColumn : grid.getGridColumns()) {
                    if(gridColumn.getType().equalsIgnoreCase("num-fmt") && gridColumn.getNumberFormat() != null) {
                        NumberFormat numberFormat = gridConfiguration.findNumberFormatByName(gridColumn.getNumberFormat());
                        if(numberFormat == null){
                            logger.error("Grid configuration '{}' - column '{}' specifies a format '{}' - cant find the definition of this format in the definitions of the number formats in the json file", grid.getName(), gridColumn.getTitle(), gridColumn.getNumberFormat());
                            gridColumn.setType("num");
                            continue;
                        }
                        gridColumn.setFormat(numberFormat.getFormat());
                    }
                }
            }

        }catch (Exception e){
            throw new RuntimeException(e);
            //FIXME - handle exception
        }
    }
    @Override
    public IGridReport getGrid(String gridName, Map<String, Object> parameters) {
        Grid grid = gridConfiguration.findGridByName(gridName);
        if(grid == null){
            logger.error("Unable to find grid by name '{}", gridName);
            throw new RuntimeException("Unable to find grid by name " + gridName); //FIXME - handle correctly
        }

        List data = executeQuery(grid.getNamedQuery(), parameters);
        GridReport gridReport = new GridReport();
        gridReport.setKey(grid.getKey());
        gridReport.setColumns(grid.getGridColumns());
        gridReport.setData(data);
        return gridReport;
    }
    Pattern paramPattern = Pattern.compile(":(\\w*)");

    /* Ideally we can just get the query parameters from the Query - however query.getParameters() returns an iterator and there
       is no guarantee on the order of the parameter - which means the user will potentially see the parameters in a different
       order everytime. To avoid this, we get the query string and parse out the parameters and show the user the parameters in
       the same order as they appear in the query string
     */
    private List<Parameter<?>> getQueryParameters(Query query){
        List<Parameter<?>> parameters = new ArrayList<>();
        List<String> parameterNames = new ArrayList<>();
        String queryString = query.unwrap(org.hibernate.Query.class).getQueryString();
        Matcher m = paramPattern.matcher(queryString);
        while (m.find( )) {
            if(parameterNames.contains(m.group(1)))continue;
            parameterNames.add(m.group(1));
            parameters.add(query.getParameter(m.group(1)));
        }
        return parameters;
    }
    @Override
    public List<IReportParameter> getParameters(String gridName, Map<String, Object> parameters){
        List<IReportParameter> reportParameters = new ArrayList<>();
        Grid grid = gridConfiguration.findGridByName(gridName);
        if(grid == null){
            logger.error("Unable to find grid by name '{}", gridName);
            throw new RuntimeException("Unable to find grid by name " + gridName); //FIXME - handle correctly
        }
        Query query = em.createNamedQuery(grid.getNamedQuery());
        if(grid.getParameters() == null){
            grid.setParameters(getQueryParameters(query));
        }

        for (Parameter<?> parameter : grid.getParameters()) {
            GridParameter gridParameter = gridConfiguration.findParameterByName(parameter.getName());
            if(gridParameter != null && gridParameter.isHidden())continue;   //Dont send hidden param to the UI
            boolean multiSelect = gridParameter == null ? false: gridParameter.isMultiSelect();
            List<Lov> lovs;
            if(gridParameter != null && gridParameter.getNamedQuery() != null) {
                lovs = getLovsFromNamedQuery(gridParameter.getNamedQuery(), parameters);
            } else if(gridParameter != null && gridParameter.getList() != null){
                lovs = getLovsFromList(gridParameter.getList());
            } else {
                /* We will come here is the type is text, enum, date etc - the getLovsFromEnum will only return LOVs for enum class else null */
                lovs = getLovsFromEnum(parameter.getParameterType());
            }
            reportParameters.add(new ReportParameter(parameter.getName(), parameter.getParameterType().getName(), true, true, multiSelect, true, lovs, gridParameter == null ? null : gridParameter.getSetTime() ));
        }
        return reportParameters;
    }

    private List<Lov> getLovsFromList(List<String> list) {
        List<Lov> lovs = list.stream().map(Lov::new).collect(Collectors.toList());
        return lovs;
    }
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
            GridParameter gridParameter = gridConfiguration.findParameterByName(parameter.getName());
            Object paramValue = parameters.get(parameter.getName());
            if(gridParameter != null && gridParameter.isHidden()) paramValue = getContextValue(gridParameter.getBean(), gridParameter.getMethod());
            if(paramValue == null){
                logger.error("The named query '{}' is missing a parameter called '{}'", namedQuery, parameter.getName());
                throw new SpringrollException(null, "missing.namedquery.parameter", parameter.getName());
            }
            try {
                Object o;
                if(gridParameter != null && gridParameter.isMultiSelect() && paramValue instanceof Collection){
                    o = new ArrayList<>();
                    Iterator iterator = ((Collection) paramValue).iterator();
                    while (iterator.hasNext()){
                        ((List)o).add(springrollUtils.convert(iterator.next(), parameter.getParameterType()));
                    }
                } else {
                    o = springrollUtils.convert(paramValue, parameter.getParameterType());
                }
                query.setParameter(parameter.getName(), o);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException(e);//FIXME
            }
        }
        return query.getResultList();
    }

    private Object getContextValue(String bean, String methodName){
        try {
            if("SpringrollUser".equalsIgnoreCase(bean)) {
                Method method = SpringrollSecurity.getUser().getClass().getMethod(methodName);
                return method.invoke(SpringrollSecurity.getUser());
            }
            Object beanInstance = applicationContext.getBean(bean);
            return beanInstance.getClass().getMethod(methodName).invoke(beanInstance);
        }catch (Exception e){
            logger.error("Exception while invoking method {} - error is {}", methodName, e.getMessage());
        }
        return null;
    }
}
