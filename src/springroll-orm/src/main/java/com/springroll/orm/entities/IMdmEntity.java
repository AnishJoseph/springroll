package com.springroll.orm.entities;

import java.util.List;

/**
 * Created by anishjoseph on 22/11/16.
 */
public interface IMdmEntity {
    List<Reviews> getReviewLogs();

    void setReviewLogs(List<Reviews> reviewLogs);

}
