/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 04.03.2021 @ 13:41:14
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * LoopCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;

public class LoopCommand extends ICommand {

    public LoopCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild guild = Bot.getInstance().getDVBL();
        AudioPlayer player = Bot.getInstance().getMusicController().getPlayer(guild);

        GuildVoiceState gvs;
        if ((gvs = commandSender.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = gvs.getChannel()) != null) {
                AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(guild).getPlayingTrack();
                AudioManager manager = vc.getGuild().getAudioManager();

                if (Bot.getInstance().getMusicController().getPlayer(guild).getPlayingTrack() != null) {

                    if (manager.isConnected()) {

                        Bot.getInstance().getMusicController().setLoopMode(!Bot.getInstance().getMusicController().isLoopMode());

                        if (Bot.getInstance().getMusicController().isLoopMode()) {
                            message.addReaction("U+2714").queue();
                        }

                        if (!Bot.getInstance().getMusicController().isLoopMode()) {
                            message.addReaction("U+274C").queue();
                        }
                    } else {
                        Bot.getInstance().getMessageManager().printBotErrorVoiceChannel(commandSender, textChannel);
                    }
                }
            } else {
                Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorPlayingSong(commandSender, textChannel);
        }
    }
}
