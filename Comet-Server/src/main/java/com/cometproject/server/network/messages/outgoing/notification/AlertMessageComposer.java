package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class AlertMessageComposer extends MessageComposer {
    private final String message;
    private final String link;

    public AlertMessageComposer(final String message, final String link) {
        this.message = message;
        this.link = link;
    }

    public AlertMessageComposer(final String message) {
        this(message, "");
    }

    @Override
    public short getId() {
        return Composers.AlertNotificationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.message);
        msg.writeString(this.link);
    }
}
