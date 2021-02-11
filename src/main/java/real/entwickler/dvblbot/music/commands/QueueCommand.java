/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 11.02.2021 @ 15:07:25
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * QueueCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;

import java.util.ArrayList;
import java.util.List;

public class QueueCommand extends ICommand {

    public QueueCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild guild = message.getGuild();
        if (Bot.getInstance().getMusicController().isIdle(guild)) {
            Bot.getInstance().getMessageManager().printBotErrorVoiceChannel(commandSender, textChannel);
            return;
        } else {
            if (Bot.getInstance().getMusicController().isQueueFilled(guild)) {
                Bot.getInstance().getMessageManager().printBotQueueEmpty(commandSender, textChannel);
            }
        }

        int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

        List<String> tracks = new ArrayList<>();
        List<String> trackSublist;

        Bot.getInstance().getMusicController().getManager(guild).getQueue().forEach(audioInfo -> tracks.add(Bot.getInstance().getMusicController().buildQueueMessage(audioInfo)));

        if (tracks.size() > 20)
            trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
        else
            trackSublist = tracks;

        String out = String.join("\n", trackSublist);
        int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;

        textChannel.sendMessage(
                new EmbedBuilder()
                        .setDescription(
                                "**CURRENT QUEUE:**\n" +
                                        "*[" + Bot.getInstance().getMusicController().getManager(guild).getQueue().size() + " Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*" +
                                        out
                        )
                        .build()
        ).queue();
    }
}
