package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.DiceFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.google.common.collect.Lists;
import org.apache.http.impl.conn.Wire;

import java.util.List;


public class WiredActionToggleState extends WiredActionItem {
    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionToggleState(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 0;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        List<Position> tilesToUpdate = Lists.newArrayList();

        for (long itemId : this.getWiredData().getSelectedIds()) {
            final RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null || floorItem instanceof WiredFloorItem || floorItem instanceof DiceFloorItem)
                continue;

            floorItem.onInteract(null, 0, true);
            tilesToUpdate.add(new Position(floorItem.getPosition().getX(), floorItem.getPosition().getY()));
        }

        for (Position tileToUpdate : tilesToUpdate) {
            this.getRoom().getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
        }

        tilesToUpdate.clear();
    }
}
