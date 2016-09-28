package com.springroll.orm.repositories;

import com.springroll.orm.entities.ReviewRules;

import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface ReviewRulesRepository extends AbstractEntityRepository<ReviewRules>{
   List<ReviewRules> findByRuleName(String ruleName);

}
