package com.springroll.notification;

import com.springroll.core.ContextStore;
import com.springroll.core.notification.INotificationManager;
import org.cometd.annotation.Listener;
import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

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
public class PushServices
{
    @Inject private BayeuxServer bayeux;
    @Session private ServerSession serverSession;
    @Autowired INotificationManager notificationManager;

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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ContextStore.put(user, null, null);

        addSubscription((String)message.get("subscription"), user.getUsername(), remote.getId());
    }

    public void deliver(List<String> userIds, Object message, String channel){
        try {
            Map<String, Set<String>> channelSubscribers = channel2User.get(channel);
            if (channelSubscribers == null) return;

            for (String userId : userIds) {
                Set<String> remoteIdsOfUser = channelSubscribers.get(userId);
                if (remoteIdsOfUser == null) continue;
                for (String remoteIdOfUser : remoteIdsOfUser) {
                    ServerSession session = bayeux.getSession(remoteIdOfUser);
                    if (session == null) continue;
                    session.deliver(serverSession, channel, message);
                }
            }
        }catch (Exception e){
            //FIXME - log message here abou the failure to deliver
        }
    }

    public void addSubscription(String serviceUri, String userId, String clientId) {
        if(!channel2User.containsKey(serviceUri)) channel2User.put(serviceUri, new HashMap<>());
        Map<String, Set<String>> channelSubscribers = channel2User.get(serviceUri);
        if(!channelSubscribers.containsKey(userId))channelSubscribers.put(userId, new HashSet<>());
        Set<String> remoteIdsOfUser = channelSubscribers.get(userId);
        remoteIdsOfUser.add(clientId);

        notificationManager.pushPendingNotifications(serviceUri);

    }
}