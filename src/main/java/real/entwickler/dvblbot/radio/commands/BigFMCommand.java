/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 02.03.2021 @ 21:15:55
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BigFMCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.radio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.AudioInfo;
import real.entwickler.dvblbot.utils.EmbedMessage;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;
import java.util.Arrays;


public class BigFMCommand extends ICommand {

    public BigFMCommand(String name, String description, String... roles) {
        super(name, description, roles);

    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild g = Bot.getInstance().getDVBL();
        if (args.length == 1) {
            if (message.getContentRaw().equalsIgnoreCase(".bf")) {
                Bot.getInstance().getMusicController().loadTrack("http://streams.bigfm.de/bigfm-deutschland-128-mp3", commandSender, message);
                Bot.getInstance().getMessageManager().printBigFMRadioLoaded(commandSender, textChannel);
            }
            if (message.getContentRaw().equalsIgnoreCase(".bigfm")) {
                Bot.getInstance().getMusicController().loadTrack("http://streams.bigfm.de/bigfm-deutschland-128-mp3", commandSender, message);
                Bot.getInstance().getMessageManager().printBigFMRadioLoaded(commandSender, textChannel);
            }
            if (message.getContentRaw().equalsIgnoreCase(".bfm")) {
                Bot.getInstance().getMusicController().loadTrack("https://ilr.bigfm.de/bigfm-mashup-128-mp3", commandSender, message);
                Bot.getInstance().getMessageManager().printBigFMMashupRadioLoaded(commandSender, textChannel);
            }
            if (message.getContentRaw().equalsIgnoreCase(".bigfm mashup")) {
                Bot.getInstance().getMusicController().loadTrack("https://ilr.bigfm.de/bigfm-mashup-128-mp3", commandSender, message);
                Bot.getInstance().getMessageManager().printBigFMMashupRadioLoaded(commandSender, textChannel);
            }
        }
    }
}
