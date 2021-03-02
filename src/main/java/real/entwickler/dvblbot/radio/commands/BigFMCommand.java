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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.EmbedMessage;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;


public class BigFMCommand extends ICommand {

    public BigFMCommand(String name, String description, String... roles) {
        super(name, description, roles);

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild g = Bot.getInstance().getDVBL();
        if (args.length == 1) {
            Bot.getInstance().getMusicController().loadTrack("https://ilr.bigfm.de/bigfm-mashup-128-mp3", commandSender, message, null);
        }
        if (Bot.getInstance().getMusicController().isIdle(Bot.getInstance().getDVBL())) return;
        message.addReaction("U+1F3B6").queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        embedBuilder.setTitle("Now playing:", "https://ilr.bigfm.de/bigfm-mashup-128-mp3");
        embedBuilder.setDescription("Viel Spaß mit BigFM Mashup!");
        embedBuilder.setColor(Color.GREEN);
    }
}
