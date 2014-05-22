package com.cometproject.server.game.rooms.types.components.games.banzai;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import javolution.util.FastMap;

import java.util.Map;

public class BanzaiGame extends RoomGame {
    public static final String TEAM_ATTRIBUTE = "gameTeam";
    public static final String SCORE_ATTRIBUTE = "gameScore";

    private Map<GameTeam, Integer> scores;

    public BanzaiGame(Room room) {
        super(room, GameType.BANZAI);

        scores = new FastMap<>();

        for (GameTeam team : GameTeam.values()) {
            this.scores.put(team, 0);
        }
    }

    @Override
    public void tick() {
        for (RoomItemFloor item : room.getItems().getByInteraction("bb_timer")) {
            item.setExtraData((gameLength - timer) + "");
            item.sendUpdate();
        }

        this.getLog().debug("Game tick (" + this.timer + ")");
    }

    @Override
    public void gameEnds() {

        GameTeam winningTeam = this.winningTeam();

        for (RoomItemFloor item : this.room.getItems().getByInteraction("bb_patch")) {
            if (item.hasAttribute(TEAM_ATTRIBUTE)) {
                if (item.getAttribute(TEAM_ATTRIBUTE).equals(winningTeam)) {
                    // TODO: this
                } else {
                    item.setExtraData("0");
                    item.sendUpdate();
                }
            } else {
                item.setExtraData("0");
                item.sendUpdate();
            }

            item.dispose();
        }

        for (GenericEntity entity : this.room.getEntities().getEntitiesCollection().values()) {
            if (entity.getEntityType().equals(RoomEntityType.PLAYER)) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (this.getTeam(playerEntity.getPlayerId()).equals(winningTeam)) {
                    this.room.getEntities().broadcastMessage(ActionMessageComposer.compose(playerEntity.getVirtualId(), 1)); // wave o/
                }
            }
        }

        this.scores.clear();

        // TODO: Wired trigger GAME_ENDS
    }

    @Override
    public void gameStarts() {
        for (RoomItemFloor item : this.room.getItems().getByInteraction("bb_patch")) {
            item.setExtraData("1");
            item.sendUpdate();
        }
    }

    public void captureTile(int x, int y, GameTeam team) {
        if (!this.active)
            return;

        for (RoomItemFloor item : this.room.getItems().getItemsOnSquare(x, y)) {
            if (item.getDefinition().getInteraction().equals("bb_patch")) {
                int itemScore = 1;

                if (item.hasAttribute(TEAM_ATTRIBUTE) && item.getAttribute(TEAM_ATTRIBUTE).equals(team)) {
                    itemScore = (int) item.getAttribute(SCORE_ATTRIBUTE) + 1;
                } else {
                    if (!item.hasAttribute(SCORE_ATTRIBUTE)) {
                        item.setAttribute(TEAM_ATTRIBUTE, team);
                    } else {
                        if ((int) item.getAttribute(SCORE_ATTRIBUTE) == 3)
                            return;
                    }
                }

                if (itemScore <= 3) {
                    item.setAttribute(SCORE_ATTRIBUTE, itemScore);
                    item.setExtraData((itemScore + (team.getTeamId() * 3) - 1) + "");

                    item.sendUpdate();

                    this.scores.replace(team, this.scores.get(team) + 1);

                    for (RoomItemFloor scoreboard : room.getItems().getByInteraction("%_score")) {
                        if (scoreboard.getDefinition().getInteraction().toUpperCase().startsWith(team.name())) {
                            scoreboard.setExtraData(this.scores.get(team) + "");
                            scoreboard.sendUpdate();
                        }
                    }
                }
            }
        }
    }

    public GameTeam winningTeam() {
        Map.Entry<GameTeam, Integer> winningTeam = null;

        for (Map.Entry<GameTeam, Integer> score : this.scores.entrySet()) {
            if (winningTeam == null || winningTeam.getValue() < score.getValue()) {
                winningTeam = score;
            }
        }

        return winningTeam != null ? winningTeam.getKey() : GameTeam.NONE;
    }
}
