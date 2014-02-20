package com.cometsrv.game.rooms.entities;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.Pathfinder;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.entities.types.BotEntity;
import com.cometsrv.game.rooms.entities.types.PetEntity;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import javolution.util.FastMap;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public abstract class GenericEntity implements AvatarEntity {
    private int id;

    private RoomEntityType entityType;

    private Position3D position;
    private Position3D walkingGoal;
    private Position3D positionToSet;

    private int bodyRotation;
    private int headRotation;

    private int roomId;
    private WeakReference<Room> room;

    private List<Square> processingPath;
    private List<Square> walkingPath;

    private Square futureSquare;

    private Pathfinder pathfinder;

    private int idleTime;
    private int signTime;

    private int danceId;
    private int effectId;

    private boolean markedNeedsUpdate;

    private Map<String, String> statusses = new FastMap<>();

    public GenericEntity(int identifier, Position3D startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        this.id = identifier;

        // Set the entity type
        if (this instanceof PlayerEntity) {
            this.entityType = entityType.PLAYER;
        } else if (this instanceof BotEntity) {
            this.entityType = entityType.BOT;
        } else if (this instanceof PetEntity) {
            this.entityType = entityType.PET;
        }

        this.position = startPosition;

        this.bodyRotation = startBodyRotation;
        this.headRotation = startHeadRotation;

        this.roomId = roomInstance.getId();
        this.room = new WeakReference<>(roomInstance);

        this.idleTime = 0;
        this.signTime = 0;

        this.danceId = 0;
        this.effectId = 0;

        this.markedNeedsUpdate = false;
    }

    @Override
    public int getVirtualId() {
        return this.id;
    }

    public RoomEntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public Position3D getPosition() {
        return this.position;
    }

    @Override
    public Position3D getWalkingGoal() {
        if (this.walkingGoal == null) {
            return this.position;
        } else {
            return this.walkingGoal;
        }
    }

    @Override
    public void setWalkingGoal(int x, int y) {
        if (this.walkingGoal == null) {
            this.walkingGoal = new Position3D(x, y, 0.0);
        } else {
            this.walkingGoal.setX(x);
            this.walkingGoal.setY(y);
        }
    }

    @Override
    public void setPosition(Position3D pos) {
        if (this.position == null) {
            this.position = pos;
        } else {
            this.position.setX(pos.getX());
            this.position.setY(pos.getY());
            this.position.setZ(pos.getZ());
        }
    }

    @Override
    public Position3D getPositionToSet() {
        return this.positionToSet;
    }

    @Override
    public void updateAndSetPosition(Position3D pos) {
        this.positionToSet = pos;
    }

    @Override
    public void markPositionIsSet() {
        this.positionToSet = null;
    }

    public boolean hasPositionToSet() {
        return (this.positionToSet != null);
    }

    @Override
    public int getBodyRotation() {
        return this.bodyRotation;
    }

    @Override
    public void setBodyRotation(int rotation) {
        this.bodyRotation = rotation;
    }

    @Override
    public int getHeadRotation() {
        return this.headRotation;
    }

    @Override
    public void setHeadRotation(int rotation) {
        this.headRotation = rotation;
    }

    @Override
    public Room getRoom() {
        return this.room.get();
    }

    @Override
    public List<Square> getProcessingPath() {
        return this.processingPath;
    }

    @Override
    public void setProcessingPath(List<Square> path) {
        this.processingPath = path;
    }

    @Override
    public List<Square> getWalkingPath() {
        return this.walkingPath;
    }

    @Override
    public void setWalkingPath(List<Square> path) {
        if(this.walkingPath != null) {
            this.walkingPath.clear();
        }

        this.walkingPath = path;
    }

    @Override
    public boolean isWalking() {
        return (this.processingPath != null) && (this.processingPath.size() > 0);
    }

    @Override
    public Square getFutureSquare() {
        return this.futureSquare;
    }

    @Override
    public void setFutureSquare(Square square) {
        this.futureSquare = square;
    }

    @Override
    public Pathfinder getPathfinder() {
        if (this.pathfinder == null) {
            this.pathfinder = new Pathfinder(this);
        }

        return this.pathfinder;
    }

    @Override
    public Map<String, String> getStatuses() {
        return this.statusses;
    }

    @Override
    public void addStatus(String key, String value) {
        if (this.statusses.containsKey(key)) {
            return;
        }

        this.statusses.put(key, value);
    }

    @Override
    public void removeStatus(String key) {
        if (!this.statusses.containsKey(key)) {
            return;
        }

        this.statusses.remove(key);
    }

    @Override
    public boolean hasStatus(String key) {
        return this.statusses.containsKey(key);
    }

    @Override
    public void markNeedsUpdate() {
        this.markedNeedsUpdate = true;
    }

    public void markNeedsUpdateComplete() {
        this.markedNeedsUpdate = false;
    }

    @Override
    public boolean needsUpdate() {
        return this.markedNeedsUpdate;
    }

    @Override
    public int getIdleTime() {
        return this.idleTime;
    }

    @Override
    public boolean isIdleAndIncrement() {
        this.idleTime++;

        if (this.idleTime >= 600) {
            if (this.idleTime > 600) {
                this.idleTime = 600;
            }

            return true;
        }

        return false;
    }

    @Override
    public void resetIdleTime() {
        this.idleTime = 0;
    }

    @Override
    public void setIdle() {
        this.idleTime = 600;
    }

    // Should call 'resetIdleTime()' instead of this method
    public void unIdle() {
        this.resetIdleTime();
    }

    @Override
    public int getSignTime() {
        return this.signTime;
    }

    @Override
    public void markDisplayingSign() {
        this.signTime = 6;
    }

    @Override
    public boolean isDisplayingSign() {
        this.signTime--;

        if (this.signTime <= 0) {
            if (this.signTime < 0) {
                this.signTime = 0;
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getDanceId() {
        return this.danceId;
    }

    @Override
    public void setDanceId(int danceId) {
        this.danceId = danceId;
    }

    @Override
    public int getCurrentEffect() {
        return this.effectId;
    }

    @Override
    public void applyEffect(int effectId) {
        this.effectId = effectId;
    }

    public abstract void joinRoom(Room room, String password);
    protected abstract void finalizeJoinRoom();

    public abstract void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView);
    protected abstract void finalizeLeaveRoom();

    public abstract boolean onChat(String message);
}
