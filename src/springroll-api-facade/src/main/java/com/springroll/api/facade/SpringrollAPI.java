package com.springroll.api.facade;

import com.springroll.router.review.ReviewActionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class SpringrollAPI extends AbstractAPI {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollAPI.class);

    @RequestMapping(value = "/sr/reviewaction", method = RequestMethod.POST)
    public Long reviewaction(@RequestBody ReviewActionDTO reviewActionDTO) {
        return route(reviewActionDTO);
    }
}
