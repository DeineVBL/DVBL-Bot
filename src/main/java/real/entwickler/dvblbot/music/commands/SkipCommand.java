/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 11.02.2021 @ 16:13:59
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * SkipCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.AudioInfo;
import real.entwickler.dvblbot.utils.ICommand;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class SkipCommand extends ICommand {

    public SkipCommand(String name, String description, String... roles) {
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

            }
        }

        if (Bot.getInstance().getMusicController().isIdle(guild)) return;
        if (args.length == 1) {
            Bot.getInstance().getMusicController().getPlayer(guild).stopTrack();
            message.addReaction("U+23E9").queue();
        } else {
            System.out.println(1);
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                System.out.println(2);
                Bot.getInstance().getMusicController().getPlayer(guild).stopTrack();
                message.addReaction("U+23E9").queue();
            }

        }
        AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(guild).getPlayingTrack();
        Bot.getInstance().getMessageManager().printPlayingSongMessage(audioTrack, commandSender, textChannel);
    }
}

