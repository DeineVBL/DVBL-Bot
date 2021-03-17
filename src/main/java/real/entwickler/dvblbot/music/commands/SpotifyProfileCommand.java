/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 13.03.2021 @ 12:28:57
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * SpotifyProfileCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.ExternalUrl;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.data.users_profile.GetUsersProfileRequest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.hc.core5.http.ParseException;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;
import real.entwickler.dvblbot.utils.Property;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SpotifyProfileCommand extends ICommand {


    public SpotifyProfileCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {

        Property property = Bot.getInstance().getProperty();

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(property.get("cfg", "client_id"))
                .setClientSecret(property.get("cfg", "client_secret"))
                .setRefreshToken(property.get("cfg", "refresh_token"))
                .build();

        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh()
                .build();

        {
            try {
                spotifyApi = new SpotifyApi.Builder()
                        .setAccessToken(authorizationCodeRefreshRequest.execute().getAccessToken())
                        .build();
            } catch (IOException | SpotifyWebApiException | ParseException e) {
                e.printStackTrace();
            }
        }

        String userId = args[1];

        if (args[1].contains("swausb")) {
            userId = "clepw269o1gvhvi3meejicm1j";
        }

        GetUsersProfileRequest getUsersProfileRequest = spotifyApi.getUsersProfile(userId)
                .build();

        try {
            CompletableFuture<com.wrapper.spotify.model_objects.specification.User> userFuture = getUsersProfileRequest.executeAsync();

            User user = userFuture.join();

            String externalURL = "https://open.spotify.com/user/" + userId;
            String profilePic = Arrays.stream(user.getImages()).findFirst().get().getUrl();

            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
            builder.setTitle("Spotify Profile [" + user.getDisplayName() + "]", externalURL);
            builder.setColor(Color.GREEN);
            builder.setThumbnail(profilePic);
            builder.setDescription("Followers: " + user.getFollowers().getTotal());
            builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
            textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F50A").queue());

        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}
