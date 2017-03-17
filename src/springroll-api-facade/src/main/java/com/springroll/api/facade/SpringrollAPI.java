package com.springroll.api.facade;

import com.springroll.core.PropertiesUtil;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.TemplateService;
import com.springroll.core.services.mdm.IMdmData;
import com.springroll.core.services.mdm.MdmService;
import com.springroll.core.services.notification.NotificationService;
import com.springroll.core.services.reporting.GridReportingService;
import com.springroll.core.services.reporting.IGridReport;
import com.springroll.core.services.reporting.IReportParameter;
import com.springroll.core.services.review.ReviewService;
import com.springroll.mdm.MdmDTO;
import com.springroll.review.ReviewActionDTO;
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

    @Autowired private TemplateService templateService;
    @Autowired private GridReportingService gridReportingService;
    @Autowired private MdmService mdmService;
    @Autowired PropertiesUtil  propertiesUtil;
    @Autowired ReviewService reviewService;
    @Autowired NotificationService notificationService;

    @RequestMapping(value = "/sr/reviewaction", method = RequestMethod.POST)
    public Long reviewAction(@RequestBody ReviewActionDTO reviewActionDTO) {
        if(reviewActionDTO.getReviewStepId() == null){
            logger.error("Received ReviewStep ID as NULL");
            return -1l;
        }
        String serviceInstanceForReviewId = reviewService.getServiceInstanceForReviewId(reviewActionDTO.isApproved(), reviewActionDTO.getReviewStepId().get(0));
        reviewActionDTO.setServiceInstance(serviceInstanceForReviewId);
        return route(reviewActionDTO);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/sr/notification/{id}")
    public Long notificationDismissed(@PathVariable Long id){
        if(id == null){
            logger.error("Received Notification ID as NULL");
            return 1l;
        }
        return route(new NotificationAckDTO(id, notificationService.getServiceInstanceForNotificationId(id)));
    }

    @RequestMapping(value = "/sr/templates", method = RequestMethod.POST)
    public Map<String,String> getTemplates(@RequestBody String[] templates) {
        return templateService.getTemplates(Arrays.asList(templates));
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
    public List<IReportParameter> getGridParams(@PathVariable String gridName, @RequestBody Map<String, Object> parameters) {
        List<IReportParameter> params = gridReportingService.getParameters(gridName, parameters);
        return params;
    }

    @RequestMapping(value = "/sr/getGridData/{gridName}", method = RequestMethod.POST)
    public IGridReport getGridData(@PathVariable String gridName, @RequestBody Map<String, Object> parameters) {
        return gridReportingService.getGrid(gridName, parameters);
    }
    @RequestMapping(value = "/sr/mdm/masters", method = RequestMethod.GET)
    public Map<String, List<String>> getMdmMasterNames() {
        return mdmService.getMdmMasterNames();
    }

    @RequestMapping(value = "/sr/mdm/data/{master}", method = RequestMethod.POST)
    public IMdmData getMDMData(@PathVariable String master) {
        return mdmService.getData(master);
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
