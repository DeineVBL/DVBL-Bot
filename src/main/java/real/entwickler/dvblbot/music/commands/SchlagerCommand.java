/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 22.02.2021 @ 11:20:10
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * SchlagerCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;


public class SchlagerCommand extends ICommand {

    public SchlagerCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild g = Bot.getInstance().getDVBL();
        if (args.length == 1) {
            Bot.getInstance().getMusicController().loadTrack("https://www.youtube.com/playlist?list=PLPjfbSBMGHkcSDvN4Xoc0X5-Rxfhs6ob7", commandSender, message); //playlist -> {
                //Bot.getInstance().getMusicController().getManager(g).shuffleQueue();
              //  Bot.getInstance().getMessageManager().printPlaylistAddedMessage(commandSender, textChannel, playlist);
           // });

        }
    }
}