package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.notification.NotificationService;
import com.springroll.core.services.push.WebPushService;
import org.cometd.annotation.Listener;
import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

/**
 * Created by anishjoseph on 01/10/16.
 */
@Named
@Singleton
@Service("pushServices")
public class CometdGlue implements WebPushService
{
    @Inject private BayeuxServer bayeux;
    @Session private ServerSession serverSession;
    @Autowired
    NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(CometdGlue.class);

    Map<String, Map<String, Set<String>>> channel2User = new HashMap<>();

//    @Listener("/service/businessreview")
//    public void processHello(ServerSession remote, ServerMessage message)
//    {
//        System.out.println("We recieved a helo msg");
//        Map<String, Object> input = message.getDataAsMap();
//        String name = (String)input.get("name");
//
//        Map<String, Object> output = new HashMap<>();
//        output.put("greeting", "Hello, " + name);
//        remote.deliver(serverSession, "/hello", output);
//    }
    @Listener("/meta/subscribe")
    public void handleSubscribeRequest(ServerSession remote, Message message) {
        addSubscription((String)message.get("subscription"), SpringrollSecurity.getUser().getUsername(), remote.getId());
    }

    public void deliver(Set<String> userIds, Object message, String channel){
        try {
            Map<String, Set<String>> channelSubscribers = channel2User.get(channel);
            if (channelSubscribers == null) return;

            for (String userId : userIds) {
                Set<String> remoteIdsOfUser = channelSubscribers.get(userId);
                if (remoteIdsOfUser == null) continue;
                Iterator<String> iterator = remoteIdsOfUser.iterator();
                while(iterator.hasNext()){
                    String remoteIdOfUser = iterator.next();
                    ServerSession session = bayeux.getSession(remoteIdOfUser);
                    if (session == null) {
                        iterator.remove();
                        continue;
                    }
                    session.deliver(serverSession, channel, message);
                }
            }
        }catch (Exception e){
            logger.error("Unable to deliver messages over channel {}. Exception message is {}", channel, e.getMessage());
            e.printStackTrace();
        }
    }

    public void addSubscription(String serviceUri, String userId, String clientId) {
        if(!channel2User.containsKey(serviceUri)) channel2User.put(serviceUri, new HashMap<>());
        Map<String, Set<String>> channelSubscribers = channel2User.get(serviceUri);
        if(!channelSubscribers.containsKey(userId))channelSubscribers.put(userId, new HashSet<>());
        Set<String> remoteIdsOfUser = channelSubscribers.get(userId);
        remoteIdsOfUser.add(clientId);

        notificationService.pushPendingNotifications(serviceUri);

    }
}