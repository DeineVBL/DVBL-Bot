/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 19.02.2021 @ 21:14:15
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MSFSCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;

public class MSFSCommand extends ICommand {


    public MSFSCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild g = Bot.getInstance().getDVBL();
        if (args.length == 1) {
            printMSFSLinks(commandSender, textChannel);
        }
    }



    private void printMSFSLinks(Member commandSender, TextChannel textChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
        embedBuilder.setTitle("MSFS - Flight Simulator 2020");
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/MSFS.png");
        embedBuilder.setDescription("Hier eine Übersicht aller MSFS-Links:");
        embedBuilder.addField("IVAO", "https://webeye.ivao.aero/", false);
        embedBuilder.addField("SimBrief", "https://www.simbrief.com/system/login.php?ref=http%3A%2F%2Fwww.simbrief.com%2Fsystem%2Fdispatch.php", false);
        embedBuilder.addField("Luftsicherung", "https://aip.dfs.de/basicIFR/2021FEB19/03195acfb69a6a964f5dfc88e1498362.html", false);
        embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021");
        textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> exitMessage.addReaction("U+2708").queue());
    }

}