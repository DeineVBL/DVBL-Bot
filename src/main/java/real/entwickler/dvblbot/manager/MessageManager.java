/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MessageManager.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.manager;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.entities.AbstractMessage;
import net.dv8tion.jda.internal.entities.ReceivedMessage;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.enums.EChannel;
import real.entwickler.dvblbot.utils.EmbedMessage;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class MessageManager {

    private Message latestPlayingMessage;

    EmbedBuilder builder = new EmbedBuilder();

    public void printJoinMessage(String channelID, Member member) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        builder.setColor(Color.green);
        builder.setTitle(":soccer:  Es ist jemand neues auf DVBL!  :soccer:");
        builder.addField("User • 👤", "» " + member.getAsMention(), false);
        builder.addField("Hinweis • ❗", "» Bitte lese dir unsere Regeln in " + EChannel.RULES.getChannel().getAsMention() + " durch. Vielen Dank!", false);
        builder.setThumbnail(member.getUser().getAvatarUrl());
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👏🏻").queue());
    }

    public void printLeaveMessage(String channelID, User user) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        builder.setColor(Color.RED);
        builder.setTitle("Auf Wiedersehen!");
        builder.addField("User • 👤", "» " + user.getAsMention(), false);
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👋").queue());
    }

    public void printReadyMessage(String channelID) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        builder.setColor(Color.green);
        builder.setDescription("Der Bot wurde gestartet!");
        SelfUser user = Bot.getInstance().getJda().getSelfUser();
        builder.setAuthor(user.getAsTag(), "https://github.com/realEntwickler/DVBL-Bot", user.getAvatarUrl());
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👍🏻").queue());
    }

    public void printStopMessage(String channelID) {
        TextChannel textChannel = Bot.getInstance().getJda().getTextChannelById(channelID);
        builder.setColor(Color.RED);
        builder.setDescription("Der Bot wird gestoppt!");
        SelfUser user = Bot.getInstance().getJda().getSelfUser();
        builder.setAuthor(user.getAsTag(), "https://github.com/realEntwickler/DVBL-Bot", user.getAvatarUrl());
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        builder.setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        assert textChannel != null;
        textChannel.sendMessage(builder.build()).queue(message -> message.addReaction("👎🏻").queue(void2 -> Bot.getInstance().getJda().shutdownNow()));
    }

    public void handlePlayingSongMessage(AudioTrack audioTrack, Member commandSender, TextChannel textChannel) {
        if (latestPlayingMessage != null) {
            long latestMessageIdLong = textChannel.getLatestMessageIdLong();
            if (latestMessageIdLong == latestPlayingMessage.getIdLong()) {
                textChannel.editMessageById(latestMessageIdLong, new EmbedMessage("Test", "DVBL-Bot - " + commandSender.getEffectiveName(), audioTrack.getInfo().title, "", null).raw(true, audioTrack, Color.green).build()).queue();
            } else {
                printPlayingSongMessage(audioTrack, commandSender, textChannel);
            }
        } else {
            printPlayingSongMessage(audioTrack, commandSender, textChannel);
        }
    }

    private void printPlayingSongMessage (AudioTrack audioTrack, Member commandSender, TextChannel textChannel) {
        textChannel.sendMessage(new EmbedMessage("Test", "DVBL-Bot - " + commandSender.getEffectiveName(), audioTrack.getInfo().title, "", null).raw(true, audioTrack, Color.green).build()).queue(exitMessage -> {
            exitMessage.addReaction("U+1F3B6").queue();
            setLatestPlayingMessage(exitMessage);
        });
    }

    public void printSongAddedQueueMessage(AudioTrack audioTrack, Member commandSender, TextChannel textChannel) {
        Guild g = Bot.getInstance().getDVBL();
        textChannel.sendMessage(new EmbedMessage("Test", "DVBL-Bot - " + commandSender.getEffectiveName(), audioTrack.getInfo().title, "", null).raw(false, audioTrack, Color.yellow).build()).queue(exitMessage -> exitMessage.addReaction("U+2705").queue());
    }

    public void printErrorVoiceChannel(Member commandSender, TextChannel textChannel) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setColor(Color.RED);
        builder.setTitle("Fehler [ERROR 003]");
        builder.setDescription("Huch, du bist wohl in keinem Voicechannel!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printBotQueueEmpty(Member commandSender, TextChannel textChannel) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setTitle("Fehler [Error 006]");
        builder.setColor(Color.RED);
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setDescription("In der Queue sind keine Lieder vorhanden!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printBotErrorVoiceChannel(Member commandSender, TextChannel textChannel) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setTitle("Fehler [Error 005]");
        builder.setColor(Color.RED);
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setDescription("Der Bot ist aktuell in keinem Voicechannel!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printErrorPlayingSong(Member commandSender, TextChannel textChannel) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setColor(Color.RED);
        builder.setTitle("Fehler [ERROR 004]");
        builder.setDescription("Aktuell spielt kein Song!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public MessageEmbed printLyricsNotFound(Member commandSender) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setColor(Color.RED);
        builder.setTitle("Fehler [ERROR 008]");
        builder.setDescription("Es wurden keine Lyrics zu diesem Song gefunden!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        return builder.build();
    }

    public void printErrorPlayCommand(Member commandSender, TextChannel textChannel) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setColor(Color.RED);
        builder.setTitle("Fehler [ERROR 002]");
        builder.setDescription("Bitte benutze .play (Songlink) wenn du Musik abspielen möchtest!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printCommandNotFoundMessage(Member commandSender, TextChannel textChannel) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setColor(Color.RED);
        builder.setTitle("Fehler [ERROR 001]");
        builder.setDescription("Dieser Command wurde nicht gefunden!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("❌").queue());
    }

    public void printCoPilotSong(Member commandSender, TextChannel textChannel) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setTitle("DVBL Song");
        builder.setColor(Color.RED);
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setDescription("Viel Spaß mit dem besten Song aller Zeiten! <3");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F60D").queue());
    }

    public void printCurrentQueue(Member commandSender, TextChannel textChannel, String[] args) {
        Guild guild = Bot.getInstance().getDVBL();
        int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

        java.util.List<String> tracks = new ArrayList<>();
        List<String> trackSublist;

        Bot.getInstance().getMusicController().getManager(guild).getQueue().forEach(audioInfo -> tracks.add(Bot.getInstance().getMusicController().buildQueueMessage(audioInfo)));

        if (tracks.size() > 20)
            trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
        else
            trackSublist = tracks;

        String out = String.join("\n", trackSublist);
        int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        embedBuilder.setTitle("Current Queue [Page " + sideNumb + " / " + sideNumbAll + "]");
        embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        embedBuilder.setDescription(out);
        embedBuilder.setFooter("DVBL-Bot - Copyright © swausb || realEntwickler 2021 ").setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    public void printPlaylistAddedMessage(Member commandSender, TextChannel textChannel, AudioPlaylist playlist) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setTitle("Playlist");
        builder.setColor(Color.RED);
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setDescription(playlist.getTracks().size() + " Titel wurden zur Playlist hinzugefügt!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F60D").queue());
    }

    public void printSearchingLyrics(Member commandSender, TextChannel textChannel, AudioTrack track) {
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setTitle("Lyrics");
        builder.setColor(Color.RED);
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setDescription("Die Lyrics zu dem Lied " + track.getInfo().title + " werden gesucht...");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F60D").queue());
    }

    public void printInactivityTimeoutMessage(TextChannel textChannel) {
        builder.setAuthor(latestPlayingMessage.getEmbeds().get(0).getAuthor().getName());
        builder.setTitle("Timeout [Queue empty]");
        builder.setColor(Color.RED);
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setDescription("Ich habe den Channnel aufgrund von Inaktivität verlassen!");
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("👋").queue());
    }

    public Message getLatestPlayingMessage() {
        return latestPlayingMessage;
    }

    public void setLatestPlayingMessage(Message latestPlayingMessage) {
        this.latestPlayingMessage = latestPlayingMessage;
    }
}