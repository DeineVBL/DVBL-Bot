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

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.hc.core5.http.ParseException;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;
import real.entwickler.dvblbot.utils.Property;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class PlayCommand extends ICommand {

    public PlayCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

        if (commandSender.getVoiceState().getChannel() != null) {

            if (args.length >= 2) {

                if (!(input.startsWith("http://") || input.startsWith("https://"))) {

                    if (Bot.getInstance().getMusicController().isBassboostMode()) {
                        input = "ytsearch:" + input + " bassboost";
                        Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);

                        return;
                    }
                    input = "ytsearch: " + input;
                    Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);

                    return;
                }
                if (input.startsWith("https://open.spotify.com/track/")) {
                    String spotifyId = input.substring(31, 53);
                    //System.out.println("spo:" + spotifyId);
                    Track spotifyTrack = getSpotifyTrack(spotifyId);
                    Bot.getInstance().getMusicController().loadTrack("ytsearch: " + spotifyTrack.getName(), commandSender, message, null);
                    return;
                }
                Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);
                return;
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
        }
    }
    public Track getSpotifyTrack (String id) {
        try {
            Property property = Bot.getInstance().getProperty();
            return new SpotifyApi.Builder().setAccessToken(property.get("cfg", "access_token")).build().getTrack(id).build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
