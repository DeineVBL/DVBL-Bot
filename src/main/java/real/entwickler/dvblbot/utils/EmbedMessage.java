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

    public EmbedBuilder raw(AudioTrack track) {
        EmbedBuilder builder = new EmbedBuilder().setAuthor(author).setDescription(description).setColor(Color.orange).setFooter("DVBL-Bot - Copyright © swausb || realEntwickler").setTimestamp(LocalDateTime.now().atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId()));
        String query = URI.create(track.getInfo().uri).getQuery();
        String[] split = query.split("&");

        builder.setThumbnail("https://img.youtube.com/vi/" + split[0].substring(2) + "/hqdefault.jpg");
        builder.setTitle("Now playing:", track.getInfo().uri);

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
