package com.cometproject.server.game.rooms.objects.items.types.floor.boutique;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;

import java.util.Arrays;


public class MannequinFloorItem extends RoomItemFloor {
    private String name = "New Mannequin";
    private String figure = "ch-210-62.lg-270-62";
    private String gender = "m";

    public MannequinFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (!this.getExtraData().isEmpty()) {
            String[] splitData = this.getExtraData().split(";#;");
            if (splitData.length != 3) return;

            this.name = splitData[0];
            this.figure = splitData[1];
            this.gender = splitData[2];

            String[] figureParts = this.figure.split("\\.");
            String finalFigure = "";

            for (String figurePart : figureParts) {
                if (!figurePart.contains("hr") && !figurePart.contains("hd") && !figurePart.contains("he") && !figurePart.contains("ha")) {
                    finalFigure += figurePart + ".";
                }
            }

            this.figure = finalFigure.substring(0, finalFigure.length() - 1);
        }
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (isWiredTrigger || !(entity instanceof PlayerEntity))
            return;

        PlayerEntity playerEntity = (PlayerEntity) entity;

        if (this.name == null || this.gender == null || this.figure == null) return;


        String newFigure = "";

        for (String playerFigurePart : Arrays.asList(playerEntity.getFigure().split("\\."))) {
            if (!playerFigurePart.startsWith("ch") && !playerFigurePart.startsWith("lg"))
                newFigure += playerFigurePart + ".";
        }

        String newFigureParts = "";

        switch (playerEntity.getGender().toUpperCase()) {
            case "M":
                if (this.figure.equals("")) return;
                newFigureParts = this.figure;
                break;

            case "F":
                if (this.figure.equals("")) return;
                newFigureParts = this.figure;
                break;
        }

        for (String newFigurePart : Arrays.asList(newFigureParts.split("\\."))) {
            if (newFigurePart.startsWith("hd"))
                newFigureParts = newFigureParts.replace(newFigurePart, "");
        }

        if (newFigureParts.equals("")) return;

        playerEntity.getPlayer().getData().setFigure(newFigure + newFigureParts);
        
        playerEntity.getPlayer().getData().setGender(this.gender);

        playerEntity.getPlayer().getData().save();
        playerEntity.getPlayer().poof();

    }

    @Override
    public String getDataObject() {
        return this.name + ";#;" + this.figure + ";#;" + this.gender;
    }

    public String getName() {
        return name;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }
}

