package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.*;

public class NavigatorFlatListMessageComposer {
    public static Composer compose(int category, int mode, String query, Collection<Room> activeRooms, boolean limit) {
        Composer msg = new Composer(Composers.NavigatorFlatListMessageComposer);
        msg.writeInt(mode);
        msg.writeString(query);
        msg.writeInt(limit ? (activeRooms.size() > 50 ? 50 : activeRooms.size()) : activeRooms.size());

        Collections.sort((List<Room>) activeRooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return ((o2.getEntities() == null ? 0 : o2.getEntities().playerCount()) -
                        (o1.getEntities() == null ? 0 : o1.getEntities().playerCount()));
            }
        });

        List<Room> topRooms = new ArrayList<>();

        for (Room room : activeRooms) {
            if (topRooms.size() < 50 || !limit)
                topRooms.add(room);
        }

        for (Room room : topRooms) {
            //System.out.println(room.getEntities().playerCount());
            RoomWriter.write(room, msg);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);

        return msg;
    }

    public static Composer compose(int category, int mode, String query, Collection<Room> activeRooms) {
        return compose(category, mode, query, activeRooms, true);
    }
}
