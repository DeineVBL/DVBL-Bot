/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
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
import real.entwickler.dvblbot.utils.ICommand;


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
        if (Bot.getInstance().getMusicController().getManager(guild).getQueue().size() > 1) {
            if (Bot.getInstance().getMusicController().isIdle(guild)) return;
            if (args.length == 1) {
                Bot.getInstance().getMusicController().getPlayer(guild).stopTrack();
                message.addReaction("U+23E9").queue();
            } else {
                for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                    Bot.getInstance().getMusicController().getPlayer(guild).stopTrack();
                    message.addReaction("U+23E9").queue();
                }

            }
            AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(guild).getPlayingTrack();
        }
    }
}

