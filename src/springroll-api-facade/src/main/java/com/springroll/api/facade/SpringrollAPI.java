package com.springroll.api.facade;

import com.springroll.core.LocaleFactory;
import com.springroll.core.services.ITemplateManager;
import com.springroll.reporting.ReportParameter;
import com.springroll.reporting.grid.GridReport;
import com.springroll.reporting.grid.GridReporter;
import com.springroll.router.notification.NotificationAckDTO;
import com.springroll.router.review.ReviewActionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Transactional
@RestController
public class SpringrollAPI extends AbstractAPI {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollAPI.class);

    @Autowired private ITemplateManager templateManager;
    @Autowired private GridReporter gridReporter;

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
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @RequestMapping(value = "/sr/localeMessages", method = RequestMethod.GET)
    public Object getLocaleMessages() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return localeFactory.getUIMessagesAsMap(userSpecificLocaleMaker.getLocaleForUser(user.getUsername()));
        return localeFactory.getUIMessagesAsMap(_locale);
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


}
