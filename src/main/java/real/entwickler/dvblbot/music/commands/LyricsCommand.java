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

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        AudioPlayer player = Bot.getInstance().getMusicController().getPlayer(message.getGuild());
        GeniusClient geniusClient = Bot.getInstance().getGeniusClient();

        if (player.isPaused())
            if (args.length == 0) {
                System.out.println("Error");
            } else {
                System.out.println("command.lyrics.searching.title" + "command.lyrics.searching.description");
                String lyricsUrl = getLyricsUrl(String.join(" ", args), geniusClient);

                if (lyricsUrl == null) {
                    System.out.println("command.lyrics.notfound" + "phrases.nothingfound");
                } else {
                    textChannel.sendMessage(getLyricsEmbed(geniusClient, lyricsUrl).build()).queue();
                }
            }
        else {
            if (args.length == 0) {
                System.out.println("command.lyrics.searching.title" + "command.lyrics.searching.description");
                String lyricsUrl = getLyricsUrl(player.getPlayingTrack().getInfo().title, geniusClient);

                if (lyricsUrl == null)
                    System.out.println("command.lyrics.notfound" + "phrases.nothingfound");
                else
                    textChannel.sendMessage(getLyricsEmbed(geniusClient, lyricsUrl, player.getPlayingTrack().getInfo().title).build()).queue();
            } else {
                System.out.println("command.lyrics.searching.title" + "command.lyrics.searching.description");
                String lyricsUrl = getLyricsUrl(String.join(" ", args), geniusClient);

                if (lyricsUrl == null)
                    System.out.println("command.lyrics.notfound" + "phrases.nothingfound");
                else
                    textChannel.sendMessage(getLyricsEmbed(geniusClient, lyricsUrl).build()).queue();
            }
        }
    }

    private EmbedBuilder getLyricsEmbed(GeniusClient geniusClient, String lyricsUrl, String title) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(title, lyricsUrl).setColor(Color.RED);
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