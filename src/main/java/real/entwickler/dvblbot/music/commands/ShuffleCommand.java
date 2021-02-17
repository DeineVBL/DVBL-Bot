/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ShuffleCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;


public class ShuffleCommand extends ICommand {

    public ShuffleCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    private void skip(Guild g) {
        Bot.getInstance().getMusicController().getPlayer(g).stopTrack();
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

        if (Bot.getInstance().getMusicController().isIdle(guild)) return;
        Bot.getInstance().getMusicController().getManager(guild).shuffleQueue();
        message.addReaction("U+1F500").queue();
        }
    }

