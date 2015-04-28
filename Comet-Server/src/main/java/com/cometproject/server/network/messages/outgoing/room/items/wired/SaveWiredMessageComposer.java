package com.cometproject.server.network.messages.outgoing.room.items.wired;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class SaveWiredMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.SaveWiredMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
