package com.springroll.orm.repositories;

import com.springroll.orm.entities.ReviewRule;

import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface ReviewRuleRepository extends AbstractEntityRepository<ReviewRule>{
   List<ReviewRule> findByRuleName(String ruleName);

}
