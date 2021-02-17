/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * EmbedMessage.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class EmbedMessage {

    private String title;
    private String author;
    private String description;
    private String thumbmail;
    private MessageEmbed.Field[] fields;

    public EmbedMessage(String title, String author, String description, String thumbmail, MessageEmbed.Field... fields) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.thumbmail = thumbmail;
        this.fields = fields;
    }

    public EmbedBuilder raw(boolean playTrackNow, AudioTrack track, Color color) {
        EmbedBuilder builder = new EmbedBuilder().setAuthor(author).setDescription(description).setColor(color).setFooter("DVBL-Bot - Copyright © swausb || realEntwickler 2021").setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        String query = URI.create(track.getInfo().uri).getQuery();
        String[] split = query.split("&");

        builder.setThumbnail("https://img.youtube.com/vi/" + split[0].substring(2) + "/hqdefault.jpg");
        if (playTrackNow) {
            builder.setTitle("Now playing: ", track.getInfo().uri);
        } else {
            builder.setTitle("Added to queue: ", track.getInfo().uri);
        }

        if (fields != null && fields.length > 0) {
            for (MessageEmbed.Field field : fields) {
                builder.addField(field);
            }
        }

        return builder;
    }

    public MessageEmbed build(){
        EmbedBuilder builder = new EmbedBuilder().setAuthor(author).setDescription(description).setTitle(title).setThumbnail(thumbmail).setColor(Color.orange).setFooter("CoPilot-Bot - Copyright © swausb || realEntwickler")
                .setTimestamp(LocalDate.now(ZoneId.of("Europe/Berlin")));

        if (fields != null && fields.length > 0) {
            for (MessageEmbed.Field field : fields) {
                builder.addField(field);
            }
        }

        return builder.build();
    }
}
