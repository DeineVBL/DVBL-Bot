/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 09.03.2021 @ 13:46:19
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * CheckCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.MusicController;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;

public class CheckCommand extends ICommand {

    public CheckCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        printCheckMessage(commandSender, textChannel);
    }

    public void printCheckMessage (Member commandSender, TextChannel textChannel) {
        EmbedBuilder builder = new EmbedBuilder();
        Guild guild = Bot.getInstance().getDVBL();
        MusicController controller = Bot.getInstance().getMusicController();
        String input1;
        String input2;
        String input3;
        String input4;

        if (controller.isLoopMode()) {
            input1 = "✅";
        } else {
            input1 = ":x:";
        }

        if (controller.isBassboostMode()) {
            input2 = "✅";
        } else {
            input2 = ":x:";
        }

        if (controller.isAchtDAudioMode()) {
            input3 = "✅";
        } else {
            input3 = ":x:";
        }

        if (controller.isEarrapeMode()) {
            input4 = "✅";
        } else {
            input4 = ":x:";
        }

        builder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        builder.setTitle("Check");
        builder.setColor(Color.CYAN);
        builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
        builder.setDescription("Diese Modi sind aktuell aktiviert / deaktiviert!");
        builder.addField("Loop", input1, false);
        builder.addField("Bassboost", input2, false);
        builder.addField("8d Audio", input3, false);
        builder.addField("Earrape", input4, false);
        builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());

        textChannel.sendMessage(builder.build()).queue(exitMessage -> exitMessage.addReaction("U+1F60D").queue());
    }
}
