package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerEnterRoom;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupBadgesMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.*;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomPanelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.FloorItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.WallItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.ConfigureWallAndFloorMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;

import java.util.Map;


public class AddUserToRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        PlayerEntity avatar = client.getPlayer().getEntity();

        if (avatar == null) {
            return;
        }

        Room room = avatar.getRoom();

        if (room == null) {
            return;
        }

        if (!room.getProcess().isActive()) {
            room.getProcess().start();
        }

        if (!room.getItemProcess().isActive()) {
            room.getItemProcess().start();
        }

        if (client.getPlayer().getRoomFloodTime() >= 1) {
            client.send(new FloodFilterMessageComposer(client.getPlayer().getRoomFloodTime()));
        }

        Map<Integer, String> groupsInRoom = new FastMap<>();

        for (PlayerEntity playerEntity : room.getEntities().getPlayerEntities()) {
            if(playerEntity.getPlayer() != null && playerEntity.getPlayer().getData() != null) {
                if (playerEntity.getPlayer().getData().getFavouriteGroup() != 0) {
                    GroupData groupData = GroupManager.getInstance().getData(playerEntity.getPlayer().getData().getFavouriteGroup());

                    if (groupData == null)
                        continue;

                    groupsInRoom.put(playerEntity.getPlayer().getData().getFavouriteGroup(), groupData.getBadge());
                }
            }
        }

        client.send(new GroupBadgesMessageComposer(groupsInRoom));

        client.send(new RoomPanelMessageComposer(room.getId(), room.getData().getOwnerId() == client.getPlayer().getId() || client.getPlayer().getPermissions().hasPermission("room_full_control")));
        client.send(new RoomDataMessageComposer(room));

        client.send(new AvatarsMessageComposer(room));
        room.getEntities().broadcastMessage(new AvatarsMessageComposer(client.getPlayer().getEntity()));

        if(room.getEntities().getAllEntities().size() > 0)
            client.send(new AvatarUpdateMessageComposer(room.getEntities().getAllEntities().values()));

        for (GenericEntity av : client.getPlayer().getEntity().getRoom().getEntities().getAllEntities().values()) {
            if (av.getCurrentEffect() != null) {
                client.send(new ApplyEffectMessageComposer(av.getId(), av.getCurrentEffect().getEffectId()));
            }

            if (av.getDanceId() != 0) {
                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new DanceMessageComposer(av.getId(), av.getDanceId()));
            }

            if (av.getHandItem() != 0) {
                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new HandItemMessageComposer(av.getId(), av.getHandItem()));
            }
        }

        client.send(new ConfigureWallAndFloorMessageComposer(client.getPlayer().getEntity().getRoom().getData().getHideWalls(), client.getPlayer().getEntity().getRoom().getData().getWallThickness(), client.getPlayer().getEntity().getRoom().getData().getFloorThickness()));
        client.getPlayer().getMessenger().sendStatus(true, true);

        client.send(new FloorItemsMessageComposer(client.getPlayer().getEntity().getRoom()));
        client.send(new WallItemsMessageComposer(client.getPlayer().getEntity().getRoom()));

        WiredTriggerEnterRoom.executeTriggers(client.getPlayer().getEntity());

        avatar.markNeedsUpdate();
        groupsInRoom.clear();
    }
}
