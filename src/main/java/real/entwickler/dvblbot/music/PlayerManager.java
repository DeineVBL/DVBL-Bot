/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 10.02.2021 @ 11:41:02
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PlayerManager.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music;

import real.entwickler.dvblbot.Bot;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    public ConcurrentHashMap<Long, MusicController> controller;

    public PlayerManager(){
        this.controller = new ConcurrentHashMap<Long, MusicController>();
    }
    public MusicController getController(long guildid) {
        MusicController mc = null;

        if(this.controller.containsKey(guildid)) {
            mc = this.controller.get(guildid);
        }
        else {
            mc = new MusicController(Bot.getInstance().getDVBL());
            this.controller.put(guildid, mc);
        }

        return mc;
    }
    public long getGuildByPlayerHash(int hash) {
        for(MusicController controller : this.controller.values()) {
            if(controller.getPlayer().hashCode() == hash) {
                return controller.getGuild().getIdLong();
            }
        }
        return -1;
    }
}

