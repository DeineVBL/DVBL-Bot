/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 22:04:05
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MessageManager.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.manager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.enums.EChannel;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class MessageManager {

    public void printJoinMessage (String channelID, Member member) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.green);
        builder.setTitle(":soccer:  Es ist jemand neues auf DVBL!  :soccer:");
        builder.addField("User • 👤", "» " + member.getAsMention(), false);
        builder.addField("Hinweis • ❗", "» Bitte lese dir unsere Regeln in " + EChannel.RULES.getChannel().getAsMention() + " durch. Vielen Dank!", false);
        builder.setThumbnail(member.getUser().getAvatarUrl());
        builder.setFooter("DVBL-Bot - Copyright © Nils Körting-Eberhardt 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👏🏻").queue());
    }

    public void printLeaveMessage (String channelID, User user) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.red);
        builder.setTitle("Auf Wiedersehen!");
        builder.addField("User • 👤", "» " + user.getAsMention(), false);
        builder.setFooter("DVBL-Bot - Copyright © Nils Körting-Eberhardt 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👋").queue());
    }

    public void printReadyMessage (String channelID) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.green);
        builder.setDescription("Der Bot wurde gestartet!");
        SelfUser user = Bot.getInstance().getJda().getSelfUser();
        builder.setAuthor(user.getAsTag(), "https://github.com/realEntwickler/DVBL-Bot", user.getAvatarUrl());
        builder.setFooter("DVBL-Bot - Copyright © Nils Körting-Eberhardt 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👍🏻").queue());
    }

    public void printStopMessage (String channelID) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.red);
        builder.setDescription("Der Bot wird gestoppt!");
        SelfUser user = Bot.getInstance().getJda().getSelfUser();
        builder.setAuthor(user.getAsTag(), "https://github.com/realEntwickler/DVBL-Bot", user.getAvatarUrl());
        builder.setFooter("DVBL-Bot - Copyright © Nils Körting-Eberhardt 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👎🏻").queue(void2 -> Bot.getInstance().getJda().shutdownNow()));
    }

    public void printErrorVoiceChannel (Member commandSender, TextChannel textChannel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("CoPilot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/swausb/CoPilot/master/images/CoPilot.jpg");
        builder.setColor(Color.red);
        builder.setTitle("Fehler [ERROR 003]");
        builder.setDescription("Huch, du bist wohl in keinem Voicechannel!");
        builder.setFooter("CoPilot-Bot - Copyright © swausb");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printBotErrorVoiceChannel (Member commandSender, TextChannel textChannel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Copilot - " +  commandSender.getEffectiveName());
        builder.setTitle("Fehler [Error 005]");
        builder.setColor(Color.red);
        builder.setThumbnail("https://raw.githubusercontent.com/swausb/CoPilot/master/images/CoPilot.jpg");
        builder.setDescription("Der Bot ist aktuell in keinem Voicechannel!");
        builder.setFooter("CoPilot-Bot - Copyright © swausb");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printErrorStopCommand (Member commandSender, TextChannel textChannel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("CoPilot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/swausb/CoPilot/master/images/CoPilot.jpg");
        builder.setColor(Color.red);
        builder.setTitle("Fehler [ERROR 004]");
        builder.setDescription("Aktuell spielt kein Song!");
        builder.setFooter("CoPilot-Bot - Copyright © swausb");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printErrorPlayCommand (Member commandSender, TextChannel textChannel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("CoPilot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/swausb/CoPilot/master/images/CoPilot.jpg");
        builder.setColor(Color.red);
        builder.setTitle("Fehler [ERROR 002]");
        builder.setDescription("Bitte benutze .play (Songlink) wenn du Musik abspielen möchtest!");
        builder.setFooter("CoPilot-Bot - Copyright © swausb");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printCommandNotFoundMessage ( Member commandSender, TextChannel textChannel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("CoPilot");
        builder.setThumbnail("https://raw.githubusercontent.com/swausb/CoPilot/master/images/CoPilot.jpg");
        builder.setColor(Color.red);
        builder.setTitle("Fehler [ERROR 001]");
        builder.setDescription("Dieser Command wurde nicht gefunden!");
        builder.setFooter("CoPilot-Bot - Copyright © swausb");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printCoPilotSong (Member commandSender, TextChannel textChannel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Copilot - " +  commandSender.getEffectiveName());
        builder.setTitle("CoPilot Song");
        builder.setColor(Color.red);
        builder.setThumbnail("https://raw.githubusercontent.com/swausb/CoPilot/master/images/CoPilot.jpg");
        builder.setDescription("Viel Spaß mit dem besten Song aller Zeiten! <3");
        builder.setFooter("CoPilot-Bot - Copyright © swausb");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F60D").queue());
        textChannel.addReactionById(textChannel.getLatestMessageId(), "U+1F3B6");
    }
}