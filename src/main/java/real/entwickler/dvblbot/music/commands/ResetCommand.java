/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 09.03.2021 @ 13:01:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ResetCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.MusicController;
import real.entwickler.dvblbot.music.TrackManager;
import real.entwickler.dvblbot.utils.ICommand;

public class ResetCommand extends ICommand {

    public ResetCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild guild = Bot.getInstance().getDVBL();
        MusicController controller = Bot.getInstance().getMusicController();
        AudioManager audioManager = commandSender.getGuild().getAudioManager();
        AudioPlayer audioPlayer = Bot.getInstance().getMusicController().getPlayer(guild);
        TrackManager manager = Bot.getInstance().getMusicController().getManager(guild);

        audioPlayer.stopTrack();
        controller.setEarrapeMode(false);
        controller.setLoopMode(false);
        controller.setBassBoostMode(false);
        controller.setAchtDAudioMode(false);
        audioPlayer.setVolume(20);
        manager.purgeQueue();
        audioManager.closeAudioConnection();

        message.addReaction("U+1F501").queue();
    }
}
