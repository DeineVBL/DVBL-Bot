/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 25.02.2021 @ 22:58:39
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PlayCustomSong.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;


public class PlayCustomSong extends ICommand {

    public PlayCustomSong(String name, String description, String... roles) {
        super(name, description, roles);

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
    }

    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        if (args.length == 1) {
            Bot.getInstance().getMusicController().loadTrack("https://schulbox.bildung-rp.de/index.php/s/cprCRtcN3kcfHMJ/download?path=%2FMu_Pam%2F6.%20Wochenplan%2010.02.&files=Karlheinz%20Stockhausen%20Das%20zerbrochene%20Lied%20der%20Deutschen%20(Ohne%20Kommentar).mp3&downloadStartSecret=vvl3bi9coho", commandSender, message, null);
        }
    }
}
