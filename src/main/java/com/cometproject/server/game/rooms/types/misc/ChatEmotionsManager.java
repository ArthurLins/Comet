package com.cometproject.server.game.rooms.types.misc;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.RoomManager;
import javolution.util.FastMap;

import java.util.Map;

public class ChatEmotionsManager {
    private FastMap<String, ChatEmotion> emotions;
    public ChatEmotionsManager() {
        emotions = new FastMap<>();

        emotions.put(":)", ChatEmotion.Smile);
        emotions.put(";)", ChatEmotion.Smile);
        emotions.put(":]", ChatEmotion.Smile);
        emotions.put(";]", ChatEmotion.Smile);
        emotions.put("=)", ChatEmotion.Smile);
        emotions.put("=]", ChatEmotion.Smile);
        emotions.put(":-)", ChatEmotion.Smile);

        emotions.put(">:(", ChatEmotion.Angry);
        emotions.put(">:[", ChatEmotion.Angry);
        emotions.put(">;[", ChatEmotion.Angry);
        emotions.put(">;(", ChatEmotion.Angry);
        emotions.put(">=(", ChatEmotion.Angry);

        emotions.put(":o", ChatEmotion.Shocked);
        emotions.put(";o", ChatEmotion.Shocked);
        emotions.put(">;o", ChatEmotion.Shocked);
        emotions.put(">:o", ChatEmotion.Shocked);
        emotions.put(">=o", ChatEmotion.Shocked);
        emotions.put("=o", ChatEmotion.Shocked);

        emotions.put(";'(", ChatEmotion.Sad);
        emotions.put(";[", ChatEmotion.Sad);
        emotions.put(":[", ChatEmotion.Sad);
        emotions.put(";(", ChatEmotion.Sad);
        emotions.put("=(", ChatEmotion.Sad);
        emotions.put("='(", ChatEmotion.Sad);
        emotions.put(":(", ChatEmotion.Sad);
        emotions.put(":-(", ChatEmotion.Sad);

        emotions.put(";D", ChatEmotion.Laugh);
        emotions.put(":D", ChatEmotion.Laugh);
        emotions.put(":L", ChatEmotion.Laugh);

        // hehe
        emotions.put("leon", ChatEmotion.Smile);
        emotions.put("comet", ChatEmotion.Smile);
        emotions.put("java", ChatEmotion.Smile);
        emotions.put("meesha", ChatEmotion.Smile);

        emotions.put("phoenix", ChatEmotion.Sad);
        emotions.put("butterfly", ChatEmotion.Sad);

        emotions.put("matou19", ChatEmotion.Angry);
        emotions.put("mathis", ChatEmotion.Angry);

        emotions.put("minette", ChatEmotion.Shocked); // mathis' cat

        RoomManager.log.info("Loaded " + this.emotions.size() + " chat emotions");
    }

    public int getEmotion(String message) {
        for(Map.Entry<String, ChatEmotion> emotion : emotions.entrySet()) {
            if(message.toLowerCase().contains(emotion.getKey().toLowerCase())) {
                return getPacketForEmotion(emotion.getValue());
            }
        }
        return 0;
    }

    private int getPacketForEmotion(ChatEmotion e) {
        if(e.equals(ChatEmotion.Smile))
            return 1;

        else if(e.equals(ChatEmotion.Angry))
            return 2;

        else if(e.equals(ChatEmotion.Shocked))
            return 3;

        else if(e.equals(ChatEmotion.Sad))
            return 4;

        else if(e.equals(ChatEmotion.Laugh))
            return 6;

        else
            return 0;

    }
}
