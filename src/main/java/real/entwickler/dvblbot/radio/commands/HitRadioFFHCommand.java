/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 17.03.2021 @ 15:41:25
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * HitRadioFFHCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.radio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.manager.MessageManager;
import real.entwickler.dvblbot.utils.ICommand;

import java.util.Arrays;

public class HitRadioFFHCommand extends ICommand {

    public HitRadioFFHCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        MessageManager messageManager = Bot.getInstance().getMessageManager();
        if (args.length == 1) {
            GuildVoiceState gvs;
            if ((gvs = commandSender.getVoiceState()) != null) {
                VoiceChannel vc;
                if ((vc = gvs.getChannel()) != null) {
                    if (message.getContentRaw().equalsIgnoreCase(".ffh")) {
                        Bot.getInstance().getMusicController().loadTrack("http://mp3.ffh.de/fs_radioffh/hqlivestream.mp3", commandSender, message, null);
                        messageManager.printHitRadioFFHLoaded(commandSender, textChannel);
                    }
                    if (message.getContentRaw().equalsIgnoreCase(".ffh party")) {
                        Bot.getInstance().getMusicController().loadTrack("http://mp3.ffh.de/fs_ffhchannels/hqparty.mp3", commandSender, message, null);
                        messageManager.printHitRadioFFHLoaded(commandSender, textChannel);
                    }
                    if (message.getContentRaw().equalsIgnoreCase(".ffh schlager")) {
                        Bot.getInstance().getMusicController().loadTrack("http://mp3.ffh.de/fs_ffhchannels/hqschlager.mp3", commandSender, message, null);
                        messageManager.printHitRadioFFHLoaded(commandSender, textChannel);
                    }
                    if (message.getContentRaw().equalsIgnoreCase(".ffh rock")) {
                        Bot.getInstance().getMusicController().loadTrack("http://mp3.ffh.de/fs_ffhchannels/hqrock.mp3", commandSender, message, null);
                        messageManager.printHitRadioFFHLoaded(commandSender, textChannel);
                    }
                    if (message.getContentRaw().equalsIgnoreCase(".ffh brandneu")) {
                        Bot.getInstance().getMusicController().loadTrack("http://mp3.ffh.de/fs_ffhchannels/hqbrandneu.mp3", commandSender, message, null);
                        messageManager.printHitRadioFFHLoaded(commandSender, textChannel);
                    }
                }
            } else {
                messageManager.printErrorVoiceChannel(commandSender, textChannel);
            }
        }
    }
}
