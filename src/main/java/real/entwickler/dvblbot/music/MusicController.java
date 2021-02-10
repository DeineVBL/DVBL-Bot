/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 10.02.2021 @ 11:40:50
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MusicController.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import real.entwickler.dvblbot.Bot;

public class MusicController {
    private Guild guild;
    private AudioPlayer player;

    public MusicController(Guild guild) {
        this.guild = guild;
        this.player = Bot.getInstance().getAudioPlayerManager().createPlayer();

        this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
        this.player.setVolume(15);
    }

    public Guild getGuild() {
        return guild;
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}

