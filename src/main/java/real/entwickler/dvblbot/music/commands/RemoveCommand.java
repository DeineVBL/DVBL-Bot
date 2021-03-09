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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.AudioInfo;
import real.entwickler.dvblbot.music.TrackManager;
import real.entwickler.dvblbot.utils.ICommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class RemoveCommand extends ICommand {

    public RemoveCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild guild = Bot.getInstance().getDVBL();

        int position = 0;
        try {
            position = Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException ec) {
            //TODO: Nachricht, dass keine Zahl angegeben wuzrde
        }

        TrackManager manager = Bot.getInstance().getMusicController().getManager(guild);
        if (position > -1 && position > manager.getNewQueue().size()) {
            //TODO: Nachricht, dass Zahl sich außerhalb des Zahlenbereiches befindet
            System.out.println("return");
            return;
        }
        AudioInfo remove = manager.getNewQueue().remove(position);


        //TODO: Nachricht, dass geklappt hat
    }


}
