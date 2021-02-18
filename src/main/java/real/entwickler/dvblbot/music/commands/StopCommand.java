/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * StopCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */
package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;

public class StopCommand extends ICommand {
    public StopCommand(String name, String usage, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        if (args.length == 1) {
            GuildVoiceState gvs;
            if ((gvs = commandSender.getVoiceState()) != null) {
                VoiceChannel vc;
                if ((vc = gvs.getChannel()) != null) {
                    Guild g = Bot.getInstance().getDVBL();
                    AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(g).getPlayingTrack();
                    AudioManager manager = vc.getGuild().getAudioManager();

                    if(manager.isConnected()) {
                        Bot.getInstance().getMusicController().getManager(g).purgeQueue();
                        Bot.getInstance().getMusicController().getPlayer(g).stopTrack();
                        message.addReaction("U+1F6D1").queue();
                    } else {
                        Bot.getInstance().getMessageManager().printBotErrorVoiceChannel(commandSender, textChannel);
                    }
                } else {
                    Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
                }
            } else {
                Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorPlayingSong(commandSender, textChannel);
        }
    }
}

