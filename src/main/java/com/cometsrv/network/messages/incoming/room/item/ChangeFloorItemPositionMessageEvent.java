package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;
import javolution.util.FastList;

import java.util.List;

public class ChangeFloorItemPositionMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();
        int rot = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if(isOwner || hasRights) {

            try {
                FloorItem item = room.getItems().getFloorItem(id);

                float height = (float) client.getPlayer().getEntity().getRoom().getModel().getSquareHeight()[x][y];

                for(FloorItem stackItem : client.getPlayer().getEntity().getRoom().getItems().getItemsOnSquare(x, y)) {
                    if(item.getId() != stackItem.getId()) {
                        if(stackItem.getDefinition().canStack) {
                            height += stackItem.getDefinition().getHeight();
                        } else {
                            return;
                        }
                    }
                }

                List<GenericEntity> affectEntities = room.getEntities().getEntitiesAt(item.getX(), item.getY());

                for (GenericEntity entity : affectEntities) {
                    if (entity.hasStatus("sit")) {
                        entity.removeStatus("sit");

                        entity.markNeedsUpdate();
                    }
                }


                List<Position3D> tilesToUpdate = new FastList<>();

                tilesToUpdate.add(new Position3D(item.getX(), item.getY(), item.getHeight()));
                tilesToUpdate.add(new Position3D(x, y, item.getHeight()));

                for (AffectedTile tile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation()))
                {
                    tilesToUpdate.add(new Position3D(tile.x, tile.y, 0d));
                    List<GenericEntity> affectEntities0 = room.getEntities().getEntitiesAt(tile.x, tile.y);

                    for (GenericEntity entity0 : affectEntities0) {
                        if (entity0.hasStatus("sit")) {
                            entity0.removeStatus("sit");

                            entity0.markNeedsUpdate();
                        }
                    }
                }

                item.setX(x);
                item.setY(y);

                item.setRotation(rot);
                item.setHeight(height);

                List<GenericEntity> newAffectEntities0 = room.getEntities().getEntitiesAt(item.getX(), item.getY());

                for (GenericEntity entity0 : newAffectEntities0) {
                    if (!entity0.hasStatus("sit") && item.getDefinition().canSit) {
                        entity0.addStatus("sit", String.valueOf(item.getDefinition().getHeight()));
                        entity0.setBodyRotation(rot);
                        entity0.setHeadRotation(rot);
                        entity0.markNeedsUpdate();
                    }
                }

                for (AffectedTile tile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation()))
                {
                    tilesToUpdate.add(new Position3D(tile.x, tile.y, 0d));

                    List<GenericEntity> affectEntities0 = room.getEntities().getEntitiesAt(tile.x, tile.y);

                    for (GenericEntity entity0 : affectEntities0) {
                        if (!entity0.hasStatus("sit")) {
                            entity0.addStatus("sit", String.valueOf(item.getDefinition().getHeight()));
                            entity0.setBodyRotation(rot);
                            entity0.setHeadRotation(rot);

                            entity0.markNeedsUpdate();
                        }
                    }
                }

                Comet.getServer().getStorage().execute("UPDATE items SET x = " + x + ", y = " + y + ", z = " + height + ", rot = " + rot + " WHERE id = " + id);

                room.getItems().getFloorItems().remove(item);
                room.getItems().getFloorItems().add(item);
                room.getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(item, room.getData().getOwnerId()));

                for(Position3D tileToUpdate : tilesToUpdate) {
                    room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
                }
            } catch(Exception e) {
                room.log.error("Error while changing floor item position", e);
            }
        }
    }
}
