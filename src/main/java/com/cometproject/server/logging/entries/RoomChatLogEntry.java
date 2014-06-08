package com.cometproject.server.logging.entries;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.LogEntryType;
import com.cometproject.server.logging.old.LogType;

public class RoomChatLogEntry extends AbstractLogEntry {
    private int roomId;
    private int userId;
    private String message;
    private int timestamp;

    public RoomChatLogEntry(int roomId, int userId, String message) {
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
        this.timestamp = (int) Comet.getTime(); // TODO: See if this works!!! haha
    }

    @Override
    public LogEntryType getType() {
        return LogEntryType.ROOM_CHATLOG;
    }

    @Override
    public String getString() {
        return this.message;
    }

    @Override
    public int getTimestamp() {
        return this.timestamp;
    }

    @Override
    public int getRoomId() {
        return this.roomId;
    }

    @Override
    public int getUserId() {
        return this.userId;
    }}