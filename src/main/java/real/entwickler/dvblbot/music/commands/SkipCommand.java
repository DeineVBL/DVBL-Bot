/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * SkipCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.TrackManager;
import real.entwickler.dvblbot.utils.ICommand;


public class SkipCommand extends ICommand {

    public SkipCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {

        Guild guild = message.getGuild();
        if (Bot.getInstance().getMusicController().isIdle(guild)) {
            Bot.getInstance().getMessageManager().printBotErrorVoiceChannel(commandSender, textChannel);
            return;
        } else {
            if (Bot.getInstance().getMusicController().isQueueFilled(guild)) {

            }
        }

        if (Bot.getInstance().getMusicController().getManager(guild).getQueue().size() > 1) {
            if (Bot.getInstance().getMusicController().isIdle(guild)) return;
            Bot.getInstance().getMusicController().getPlayer(guild).stopTrack();
            message.addReaction("U+23E9").queue();

        } else {

            VoiceChannel vc = commandSender.getVoiceState().getChannel();

            if (vc == null) {
                Bot.getInstance().getMessageManager().printBotErrorVoiceChannel(commandSender, textChannel);
            } else {
                AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(guild).getPlayingTrack();
                AudioPlayer audioPlayer = Bot.getInstance().getMusicController().getPlayer(guild);
                AudioManager audioManagermanager = vc.getGuild().getAudioManager();
                TrackManager manager = Bot.getInstance().getMusicController().getManager(guild);

                audioPlayer.stopTrack();
                manager.purgeQueue();
                audioManagermanager.closeAudioConnection();
                message.addReaction("U+23E9").queue();
                Bot.getInstance().getMessageManager().printSkipQueueEmptyMessage(commandSender, textChannel);
            }
        }

        AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(guild).getPlayingTrack();
    }
}

