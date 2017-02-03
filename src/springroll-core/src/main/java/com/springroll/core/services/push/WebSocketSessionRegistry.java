package com.springroll.core.services.push;

import com.springroll.core.SpringrollUser;

/**
 * Created by anishjoseph on 03/02/17.
 */
public interface WebSocketSessionRegistry {
    SpringrollUser getUserForSessionId(String sessionId);
}
