/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 17.02.2021 @ 18:59:40
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * LyricsCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.FormatUtil;
import real.entwickler.dvblbot.utils.GeniusClient;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;

public class LyricsCommand extends ICommand {

    public LyricsCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {

        GeniusClient geniusClient = new GeniusClient(Bot.getInstance().getProperty().get("cfg", "genius-token"));
        AudioPlayer player = Bot.getInstance().getMusicController().getPlayer(textChannel.getGuild());

        if (player.getPlayingTrack() == null) {
            if (args.length == 1) {
                System.out.println("1");
                Bot.getInstance().getMessageManager().printErrorPlayingSong(commandSender, textChannel);
            } else {
                System.out.println("2");
                //TODO: searching lyrics message
                Bot.getInstance().getMessageManager().printSearchingLyrics(commandSender, textChannel, player.getPlayingTrack());
                String lyricsUrl = getLyricsUrl(String.join(" ", args), geniusClient);

                if (lyricsUrl == null)
                    //TODO: didnt found any lyrics
                    message.editMessage(Bot.getInstance().getMessageManager().printLyricsNotFound(commandSender)).queue(message1 -> message1.addReaction("❌").queue());
                else {
                    //TODO: send lyrics
                    message.editMessage(getLyricsEmbed(geniusClient, lyricsUrl, player.getPlayingTrack().getInfo().title).build()).queue();
                }
            }
        } else {
            if (args.length == 1) {
                System.out.println("3");
                //TODO: searching lyrics message
                Bot.getInstance().getMessageManager().printSearchingLyrics(commandSender, textChannel, player.getPlayingTrack());
                String lyricsUrl = getLyricsUrl(player.getPlayingTrack().getInfo().title, geniusClient);

                if (lyricsUrl == null) {
                    System.out.println("4");
                    //TODO: didnt found any lyrics
                    Bot.getInstance().getMessageManager().printLyricsNotFound(commandSender);
                } else {
                    //TODO: send lyrics
                    message.editMessage(getLyricsEmbed(geniusClient, lyricsUrl, player.getPlayingTrack().getInfo().title).build()).queue();
                }
            } else {
                System.out.println("5");
                //TODO: search lyrics
                Bot.getInstance().getMessageManager().printSearchingLyrics(commandSender, textChannel, player.getPlayingTrack());
                String lyricsUrl = getLyricsUrl(String.join(" ", args), geniusClient);

                if (lyricsUrl == null) {
                    //TODO: lyrics not found
                    message.editMessage(Bot.getInstance().getMessageManager().printLyricsNotFound(commandSender)).queue();
                } else {
                    //TODO: send lyrics
                    message.editMessage(getLyricsEmbed(geniusClient, lyricsUrl, player.getPlayingTrack().getInfo().title).build()).queue();
                }
            }
        }
    }

    private EmbedBuilder getLyricsEmbed(GeniusClient geniusClient, String lyricsUrl, String title) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(title, lyricsUrl).setColor(Color.DARK_GRAY);
        String[] comps = getLyrics(lyricsUrl, geniusClient);
        String[] tempLine = new String[2];
        tempLine[0] = null;
        tempLine[1] = null;
        int count = 0;
        for (String comp : comps) {
            if (count == 0 && !comp.startsWith("t:"))
                embedBuilder.setDescription(comp);
            else {
                if (comp.startsWith("t:")) {
                    comp = comp.replace("t:", "");
                    tempLine[0] = comp;
                } else {
                    if (count == 0) tempLine[0] = "\u200b";
                    tempLine[1] = comp;
                }
                if (tempLine[0] != null && tempLine[1] != null) {
                    embedBuilder.addField(tempLine[0], tempLine[1], false);
                    tempLine[0] = null;
                    tempLine[1] = null;
                }
            }
            count++;
        }
        return embedBuilder;
    }

    private EmbedBuilder getLyricsEmbed(GeniusClient geniusClient, String lyricsUrl) {
        return getLyricsEmbed(geniusClient, lyricsUrl, geniusClient.getTitle(lyricsUrl));
    }

    private String getLyricsUrl(String query, GeniusClient geniusClient) {
        return geniusClient.searchSong(query);
    }

    public String[] getLyrics(String lyricsUrl, GeniusClient geniusClient) {
        return FormatUtil.formatLyrics(geniusClient.getLyrics(lyricsUrl));
    }
}