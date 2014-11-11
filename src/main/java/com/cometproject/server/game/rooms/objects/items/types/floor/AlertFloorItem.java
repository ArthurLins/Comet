package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class AlertFloorItem extends RoomItemFloor {
    public AlertFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.ticksTimer > 0) {
            return;
        }

        this.setExtraData("1");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(1.5));
    }

    @Override
    public void onTickComplete() {
        this.setExtraData("0");
        this.sendUpdate();
    }
}
