/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 08.03.2021 @ 14:19:43
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * RemoveCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.AudioInfo;
import real.entwickler.dvblbot.music.TrackManager;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;
import java.sql.Time;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RemoveCommand extends ICommand {

    public RemoveCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild guild = Bot.getInstance().getDVBL();

        message.addReaction("U+1F4DB").queue();

        int position = 0;
        try {
            position = Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException ec) {
            Bot.getInstance().getMessageManager().printRemoveNoNumber(commandSender, textChannel);
        }

        TrackManager manager = Bot.getInstance().getMusicController().getManager(guild);
        if (position > -1 && position > manager.getNewQueue().size()) {
            Bot.getInstance().getMessageManager().printRemoveOutOfRange(commandSender, textChannel);
            return;
        }

        AudioInfo remove = manager.getNewQueue().remove(position);

        position++;

        AudioTrackInfo track = remove.getTrack().getInfo();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setColor(Color.CYAN);
        builder.setTitle("Remove [Song " + position + "]", track.uri );
        builder.addField(track.author, track.title, false);
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
        textChannel.sendMessage(builder.build()).queue(exitMessage -> {
            exitMessage.addReaction("❌").queue();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    exitMessage.delete().queue();
                }
            }, TimeUnit.SECONDS.toMillis(8));
        });
    }
}
