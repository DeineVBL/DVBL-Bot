/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 22:52:42
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IReactionRole.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.manager;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class IReactionRole extends ListenerAdapter{

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        event.getChannel().getName().equalsIgnoreCase("botex");
        event.getChannel().sendMessage(new MessageBuilder("Wenn ihr auf diese Nachricht mit den untenstehenden Reaktionen reagiert, bekommt ihr eine bestimmte Rolle zugewiesen: In diesem Fall, welche Spiele ihr spielt... Das hilft anderen Mitgliedern, leichter einen Teamkameraden zu finden, der die gleichen Spiele zockt!").build()).queue();
        }
    }




