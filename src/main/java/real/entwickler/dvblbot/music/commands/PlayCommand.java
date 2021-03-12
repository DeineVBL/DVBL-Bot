/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PlayCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.hc.core5.http.ParseException;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;
import real.entwickler.dvblbot.utils.Property;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class PlayCommand extends ICommand {

    Property property = Bot.getInstance().getProperty();

    private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(property.get("cfg", "client_id"))
            .setClientSecret(property.get("cfg", "client_secret"))
            .setRefreshToken(property.get("cfg", "refresh_token"))
            .build();

    private final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh()
            .build();


    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

        if (Objects.requireNonNull(commandSender.getVoiceState()).getChannel() != null) {

            if (args.length >= 2) {

                if (!(input.startsWith("http://") || input.startsWith("https://"))) {

                    if (Bot.getInstance().getMusicController().isBassboostMode()) {
                        input = "ytsearch:" + input + " bassboost";
                        Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);

                        return;
                    }

                    if (Bot.getInstance().getMusicController().isAchtDAudioMode()) {
                        input = "ytsearch:" + input + " 8d audio";
                        Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);

                        return;
                    }

                    input = "ytsearch: " + input;
                    Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);

                    return;
                }
                if (input.startsWith("https://open.spotify.com/track/")) {
                    String spotifyId = input.substring(31, 53);
                    Track spotifyTrack = getSpotifyTrack(spotifyId);
                    Bot.getInstance().getMusicController().loadTrack("ytsearch: " + spotifyTrack.getName(), commandSender, message, null);
                    return;
                }
                if (input.startsWith("https://open.spotify.com/playlist/")) {
                    String spotifyId = input.substring(34, 56);
                    StringBuilder stringBuilder;
                    for (PlaylistTrack item : getPlaylistsItems(spotifyId).getItems()) {

                        Track spotifyTrack = getSpotifyTrack(item.getTrack().getId());
                        ArtistSimplified[] artists = spotifyTrack.getArtists();
                        stringBuilder = new StringBuilder();

                        for (ArtistSimplified artist : artists) {
                            stringBuilder.append(artist.getName()).append(", ");
                        }
                        stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.toString().length() - 2));
                        stringBuilder.append(" - ").append(spotifyTrack.getName());
                        Bot.getInstance().getMusicController().loadPlaylist("ytsearch: " + stringBuilder.toString(), commandSender, message, null);
                    }
                    return;
                }

                Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);
            }

        } else {
            Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
        }
    }


    public PlayCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    public Track getSpotifyTrack(String id) {
        String accesstoken = null;
        try {
            accesstoken = authorizationCodeRefreshRequest.execute().getAccessToken();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
        try {
            return new SpotifyApi.Builder().setAccessToken(accesstoken).build().getTrack(id).build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Paging<PlaylistTrack> getPlaylistsItems(String spotifyId) {
        String accesstoken = null;
        try {
            accesstoken = authorizationCodeRefreshRequest.execute().getAccessToken();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
        try {
            return new SpotifyApi.Builder().setAccessToken(accesstoken).build().getPlaylistsItems(spotifyId).build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}



