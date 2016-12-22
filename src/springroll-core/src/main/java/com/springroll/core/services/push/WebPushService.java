package com.springroll.core.services.push;

import java.util.Set;

/**
 * Created by anishjoseph on 22/12/16.
 */
public interface WebPushService {
     void deliver(Set<String> userIds, Object message, String channel);
}
