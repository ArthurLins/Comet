package com.cometproject.api.networking.sessions;

import com.cometproject.api.networking.messages.IMessageComposer;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.Set;

public interface ISessionManager {

    boolean disconnectByPlayerId(int id);

    ISession getByPlayerId(int id);

    Set<ISession> getByPlayerPermission(String permission);

    ISession getByPlayerUsername(String username);

    int getUsersOnlineCount();

    Map<Integer, ISession> getSessions();

    void broadcast(IMessageComposer msg);

    void broadcastByPermission(IMessageComposer messageComposer, String permission);
}
