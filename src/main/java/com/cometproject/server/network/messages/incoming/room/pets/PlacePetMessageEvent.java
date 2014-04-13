package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PlacePetMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int petId = msg.readInt();
        PetData pet = client.getPlayer().getPets().getPet(petId);

        if(pet == null) {
            return;
        }

        // TODO: this
       // client.getPlayer().getEntity().getRoom().getEntities().addEntity(new PetEntity(pet, client.getPlayer().getEntity().getRoom().getEntities().get new Position3D(msg.readInt(), msg.readInt()), 0, 0, client.getPlayer().getEntity().getRoom()));
    }
}
