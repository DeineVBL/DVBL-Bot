package real.entwickler.dvblbot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.net.URI;

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
        net.dv8tion.jda.api.EmbedBuilder builder = new EmbedBuilder().setAuthor(author).setDescription(description).setThumbnail(thumbmail).setTitle(title).setColor(Color.orange).setFooter("CoPilot-Bot - Copyright © swausb || realEntwickler");

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
        EmbedBuilder builder = new EmbedBuilder().setAuthor(author).setDescription(description).setTitle(title).setThumbnail(thumbmail).setColor(Color.orange).setFooter("CoPilot-Bot - Copyright © swausb || realEntwickler");

        if (fields != null && fields.length > 0) {
            for (MessageEmbed.Field field : fields) {
                builder.addField(field);
            }
        }

        return builder.build();
    }
}
