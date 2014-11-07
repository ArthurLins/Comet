package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.game.rooms.types.misc.settings.RoomTradeState;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.trading.TradeErrorMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class BeginTradeMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if(client.getPlayer().getEntity() == null)
            return;

        int userId = msg.readInt();

        if(client.getPlayer().getEntity().getRoom().getData().getTradeState() == RoomTradeState.DISABLED) {
            client.send(TradeErrorMessageComposer.compose(6, ""));
            return;
        }

        PlayerEntity entity = (PlayerEntity) client.getPlayer().getEntity().getRoom().getEntities().getEntity(userId);

        if (entity == null || entity.hasStatus(RoomEntityStatus.TRADE)) {
            client.send(TradeErrorMessageComposer.compose(8, entity != null ? entity.getUsername() : "Unknown Player"));
            return;
        }

        if(client.getPlayer().getEntity().getRoom().getData().getOwnerId() != client.getPlayer().getId() && entity.getRoom().getData().getTradeState() == RoomTradeState.OWNER_ONLY) {
            client.send(TradeErrorMessageComposer.compose(6, ""));
            return;
        }

        client.getPlayer().getEntity().getRoom().getTrade().add(new Trade(client.getPlayer().getEntity(), entity));
    }
}
