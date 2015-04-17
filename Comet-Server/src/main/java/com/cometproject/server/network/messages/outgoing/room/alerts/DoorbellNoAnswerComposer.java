package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class DoorbellNoAnswerComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.DoorbellNoOneMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeString("");
    }
}