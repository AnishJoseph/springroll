package com.springroll.orm.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 30/09/16.
 */
@Component public class Repositories {
    @Autowired public UserRepository users;

    @Autowired public ReviewRuleRepository reviewRules;

    @Autowired public ReviewStepRepository reviewStep;

    @Autowired public JobRepository job;

    @Autowired public NotificationRepository notification;
}
