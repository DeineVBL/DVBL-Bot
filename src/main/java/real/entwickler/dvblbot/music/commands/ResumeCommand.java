/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ResumeCommmand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.MusicController;
import real.entwickler.dvblbot.utils.ICommand;

public class ResumeCommand extends ICommand {

    public ResumeCommand(String name, String description, String... roles) {
        super(name, description, roles);

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
    }

    private void lastTrack() {
        Bot.getInstance().getMusicController().getManager(Bot.getInstance().getDVBL()).getQueue2().element().getTrack().getInfo();
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        MusicController musicController = Bot.getInstance().getMusicController();
        Guild guild = message.getGuild();
        if (Bot.getInstance().getMusicController().isIdle(guild)) return;

        if (musicController.getPlayer(guild).isPaused()) {
            Bot.getInstance().getMusicController().getPlayer(guild).setPaused(false);
        }

        message.addReaction("▶").queue();
    }
}

