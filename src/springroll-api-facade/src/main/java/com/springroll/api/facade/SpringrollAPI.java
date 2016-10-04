package com.springroll.api.facade;

import com.springroll.core.SpringrollUser;
import com.springroll.core.services.IReviewManager;
import com.springroll.router.notification.NotificationAckDTO;
import com.springroll.router.review.ReviewActionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class SpringrollAPI extends AbstractAPI {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollAPI.class);

    @Autowired
    IReviewManager reviewManager;

    @RequestMapping(value = "/sr/reviewaction", method = RequestMethod.POST)
    public Long reviewaction(@RequestBody ReviewActionDTO reviewActionDTO) {
        String approverForReviewStep = reviewManager.getApproverForReviewStep(reviewActionDTO.getReviewStepId());
        if(approverForReviewStep == null){
            logger.error("Received invalid review step {}", reviewActionDTO.getReviewStepId());
            return -1L;
        }
        SpringrollUser user = (SpringrollUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getGroups().contains(approverForReviewStep)){
            logger.error("User '{}' is not authorized to review step '{}' - requires the user to belong to group '{}'", user.getUsername(), reviewActionDTO.getReviewStepId(), approverForReviewStep);
            return -1L;
        }
        return route(reviewActionDTO);
    }
    @RequestMapping(value = "/sr/notificationack", method = RequestMethod.POST)
    public Long reviewaction(@RequestBody NotificationAckDTO notificationAckDTO) {
        //FIXME - should we check here or later if this user is allowed to ack this message
        return route(notificationAckDTO);
    }
}
