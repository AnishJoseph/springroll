package com.springroll.api.facade;

import com.springroll.core.LocaleFactory;
import com.springroll.core.services.ITemplateManager;
import com.springroll.router.notification.NotificationAckDTO;
import com.springroll.router.review.ReviewActionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

@Transactional
@RestController
public class SpringrollAPI extends AbstractAPI {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollAPI.class);

    @Autowired private ITemplateManager templateManager;
    @Autowired private LocaleFactory localeFactory;

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

    private static Locale _locale = Locale.getDefault();

    @RequestMapping(value = "/sr/localeMessages", method = RequestMethod.GET)
    public Object getLocaleMessages() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return localeFactory.getUIMessagesAsMap(userSpecificLocaleMaker.getLocaleForUser(user.getUsername()));
        return localeFactory.getUIMessagesAsMap(_locale);
    }


}
