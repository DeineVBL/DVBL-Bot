/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 09.03.2021 @ 13:18:29
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * AchtDAudioCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.TrackManager;
import real.entwickler.dvblbot.utils.ICommand;

public class AchtDAudioCommand extends ICommand {

    public AchtDAudioCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {

        Guild g = Bot.getInstance().getDVBL();
        AudioPlayer player = Bot.getInstance().getMusicController().getPlayer(g);
        TrackManager manager = Bot.getInstance().getMusicController().getManager(g);

        Bot.getInstance().getMusicController().setAchtDAudioMode(!Bot.getInstance().getMusicController().isAchtDAudioMode());

        if (Bot.getInstance().getMusicController().isAchtDAudioMode()) {
            message.addReaction("U+2714").queue();
        }

        if (!Bot.getInstance().getMusicController().isAchtDAudioMode()) {
            message.addReaction("U+274C").queue();
        }

    }
}
