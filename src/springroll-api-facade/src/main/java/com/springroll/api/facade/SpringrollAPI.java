package com.springroll.api.facade;

import com.springroll.core.PropertiesUtil;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.ITemplateManager;
import com.springroll.mdm.MdmDTO;
import com.springroll.mdm.MdmData;
import com.springroll.mdm.MdmManager;
import com.springroll.reporting.ReportParameter;
import com.springroll.reporting.grid.GridReport;
import com.springroll.reporting.grid.GridReporter;
import com.springroll.review.ReviewActionDTO;
import com.springroll.review.ReviewManager;
import com.springroll.router.notification.NotificationAckDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Transactional
@RestController
public class SpringrollAPI extends AbstractAPI {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollAPI.class);

    @Autowired private ITemplateManager templateManager;
    @Autowired private GridReporter gridReporter;
    @Autowired private MdmManager mdmManager;
    @Autowired ReviewManager reviewManager;
    @Autowired PropertiesUtil  propertiesUtil;

    @RequestMapping(value = "/sr/reviewaction", method = RequestMethod.POST)
    public Long reviewAction(@RequestBody ReviewActionDTO reviewActionDTO) {
        if(reviewActionDTO.getReviewStepId() == null){
            logger.error("Received ReviewStep ID as NULL");
            return -1l;
        }
        return route(reviewActionDTO);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/sr/notification/{id}")
    public Long notificationDismissed(@PathVariable Long id){
        if(id == null){
            logger.error("Received Notification ID as NULL");
            return 1l;
        }
        return route(new NotificationAckDTO(id));
    }

    @RequestMapping(value = "/sr/templates", method = RequestMethod.POST)
    public Map<String,String> getTemplates(@RequestBody String[] templates) {
        return templateManager.getTemplates(Arrays.asList(templates));
    }
    @RequestMapping(value = "/sr/user", method = RequestMethod.GET)
    public Object getUser() {
        return SpringrollSecurity.getUser();
    }

    @RequestMapping(value = "/sr/localeMessages", method = RequestMethod.GET)
    public Object getLocaleMessages() {
        return localeFactory.getUIMessagesAsMap(SpringrollSecurity.getUser().getLocale());
    }

    @RequestMapping(value = "/sr/gridParams/{gridName}", method = RequestMethod.POST)
    public List<ReportParameter> getGridParams(@PathVariable String gridName, @RequestBody Map<String, Object> parameters) {
        List<ReportParameter> params = gridReporter.getParameters(gridName, parameters);
        return params;
    }

    @RequestMapping(value = "/sr/getGridData/{gridName}", method = RequestMethod.POST)
    public GridReport getGridData(@PathVariable String gridName, @RequestBody Map<String, Object> parameters) {
        GridReport grid = gridReporter.getGrid(gridName, parameters);
        return grid;
    }
    @RequestMapping(value = "/sr/mdm/masters", method = RequestMethod.GET)
    public List<String> getMdmMasterNames() {
        return mdmManager.getMdmMasterNames();
    }

    @RequestMapping(value = "/sr/mdm/data/{master}", method = RequestMethod.POST)
    public MdmData getMDMData(@PathVariable String master) {
        return mdmManager.getData(master);
    }
    @RequestMapping(value = "/sr/mdm/update", method = RequestMethod.POST)
    public Long updateMDMData(@RequestBody MdmDTO mdmDTO) {
        return route(mdmDTO);
    }

    @RequestMapping(value = "/sr/properties", method = RequestMethod.GET)
    public Map<String, String> getUIProperties() {
        return propertiesUtil.getUIProperties();
    }

}
