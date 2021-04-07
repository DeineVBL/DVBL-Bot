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

import com.jagrosh.jlyrics.Lyrics;
import com.jagrosh.jlyrics.LyricsClient;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.hc.core5.http.ParseException;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;
import real.entwickler.dvblbot.utils.Property;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class LyricsCommand extends ICommand {


    public LyricsCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {

        GuildVoiceState gvs;

        if ((gvs = commandSender.getVoiceState()) != null) {

            VoiceChannel vc;

            if ((vc = gvs.getChannel()) != null) {


                AudioManager manager = vc.getGuild().getAudioManager();

                if (manager.isConnected()) {

                    if (Bot.getInstance().getMusicController().getPlayer(Bot.getInstance().getDVBL()).getPlayingTrack() != null) {

                        Property property = Bot.getInstance().getProperty();

                        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                                .setClientId(property.get("cfg", "client_id"))
                                .setClientSecret(property.get("cfg", "client_secret"))
                                .setRefreshToken(property.get("cfg", "refresh_token"))
                                .build();


                        final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh()
                                .build();

                        String accesstoken = null;
                        try {
                            accesstoken = authorizationCodeRefreshRequest.execute().getAccessToken();
                        } catch (IOException | ParseException | SpotifyWebApiException e) {
                            e.printStackTrace();
                        }

                        spotifyApi.setAccessToken(accesstoken);

                        AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(Bot.getInstance().getDVBL()).getPlayingTrack();
                        String filteredTrackTitle = filterTrackTitle(audioTrack.getInfo().title);
                        try {
                            final Paging<Track> trackPaging = spotifyApi.searchTracks(filteredTrackTitle).build().execute();

                            Track firstTrack = Arrays.stream(trackPaging.getItems()).findFirst().orElse(null);

                            if (firstTrack != null) {

                                LyricsClient lyricsClient = new LyricsClient();
                                Lyrics lyrics;
                                try {
                                    lyrics = lyricsClient.getLyrics(firstTrack.getName() + Arrays.stream(firstTrack.getArtists()).findFirst().get().getName()).get();
                                    EmbedBuilder builder = new EmbedBuilder();

                                    if (lyrics.getContent() != null) {

                                        if (args.length == 1) {

                                            if (lyrics.getContent().length() > 2048) {

                                                String description = lyrics.getContent().substring(0, 2048);

                                                String trackuri = Bot.getInstance().getMusicController().getPlayer(Bot.getInstance().getDVBL()).getPlayingTrack().getInfo().uri;

                                                builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                                                builder.setTitle("Lyrics [" + filteredTrackTitle + "]", trackuri);
                                                builder.setColor(Color.CYAN);

                                                String query = URI.create(Bot.getInstance().getMusicController().getPlayer(Bot.getInstance().getDVBL()).getPlayingTrack().getInfo().uri).getQuery();
                                                String[] split = query.split("&");

                                                builder.setThumbnail("https://img.youtube.com/vi/" + split[0].substring(2) + "/hqdefault.jpg");

                                                builder.setDescription(description);
                                                builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                                                textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F3B6").queue());
                                            }
                                        } else if (args.length == 2) {
                                            String substring2 = lyrics.getContent().substring(2048);
                                            builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                                            builder.setTitle("Lyrics");
                                            builder.setColor(Color.CYAN);
                                            builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                                            builder.setDescription(substring2);
                                            builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                                            textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F3B6").queue());
                                        }
                                    } else {
                                        Bot.getInstance().getMessageManager().printNoLyricsFound(commandSender, textChannel);
                                    }

                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Bot.getInstance().getMessageManager().printNoLyricsFound(commandSender, textChannel);
                            }

                        } catch (IOException | SpotifyWebApiException | ParseException e) {
                            e.printStackTrace();

                        }

                    } else {
                        Bot.getInstance().getMessageManager().printErrorPlayingSong(commandSender, textChannel);
                    }
                } else {
                    Bot.getInstance().getMessageManager().printBotErrorVoiceChannel(commandSender, textChannel);
                }
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
        }
    }

    private String filterTrackTitle(String toFilter) {
        String preFinished = "";
        if (toFilter.contains("(")) {
            int bracesBegin = toFilter.indexOf("(");
            int bracesEnd = 0;
            for (int i = bracesBegin; i < toFilter.length(); i++) {
                if (toFilter.charAt(i) == ')') {
                    bracesEnd = i;
                }
            }
            preFinished = toFilter.substring(0, bracesBegin - 1) + toFilter.substring(bracesEnd + 1);
        }

        if (toFilter.contains("[")) {
            int bracesBegin = toFilter.indexOf("{");
            int bracesEnd = 0;
            for (int i = bracesBegin; i < toFilter.length(); i++) {
                if (toFilter.charAt(i) == ']') {
                    bracesEnd = i;
                }
            }
            preFinished = toFilter.substring(0, bracesBegin - 1) + toFilter.substring(bracesEnd + 1);
        }
        if (preFinished.contains("ft.")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("ft."));
        }
        if (preFinished.contains("feat.")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("feat."));
        }
        if (preFinished.contains("7")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("7"));
        }
        if (preFinished.contains("4")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("4"));
        }
        if (preFinished.contains("1")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("1"));
        }
        if (preFinished.contains("Official")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("Official"));
        }
        if (preFinished.contains("official")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("official"));
        }
        if (preFinished.contains("Music")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("Music"));
        }
        if (preFinished.contains("music")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("music"));
        }
        if (preFinished.contains("Video")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("Video"));
        }
        if (preFinished.contains("video")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("video"));
        }
        if (preFinished.contains("Remix")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("Remix"));
        }
        if (preFinished.contains("remix")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("remix"));
        }
        if (preFinished.contains("|")) {
            preFinished = preFinished.substring(0, preFinished.indexOf("|"));
        }
        return preFinished.length() == 0 ? toFilter : preFinished;
    }
}
