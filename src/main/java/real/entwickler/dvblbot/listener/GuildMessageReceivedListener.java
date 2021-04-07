/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * GuildMessageReceivedListener.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.manager.MessageManager;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class GuildMessageReceivedListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Member member = event.getMember();
        Message message = event.getMessage();

        /**
         * Überprüfen, ob die Nachricht mit einem Punkt beginnt ==> Command
         */
        if (!txtChannel.getName().equalsIgnoreCase("casino")) {

            if (txtChannel.getName().equalsIgnoreCase("commands")) {

                if (message.getContentRaw().startsWith(".")) {

                    //Bots sollen unsere Kommandos nicht ausführen! Böse! aus!!!
                    if (user.isBot())
                        return;
                    //Wir zerteilen die Nachricht in mehrere Stücke und teilen sie immer dann, wenn ein Leerzeichen kommt
                    String[] arguments = message.getContentDisplay().split(" ");

            /*
            1. Wir holen uns alle registrierten Kommandos aus unserem Command Manager
            2. Wir filtern sie danach, ob der aktuelle Kommand in der Liste den Namen hat
                -> Hierbei ignorieren wir das erste Zeichen des 1. Werts aus unserem StringArray arguments (siehe oben), damit wir wissen, was der User ohne den Punkt angegeben hat
            3. Wir holen uns das erste, was dem Filter entspricht
            4. Wenn der Filter auf keinen Kommand zutrifft, lassen wir uns null zurückgeben

             */
                    ICommand iCommand = Bot.getInstance().getCommandManager().getRegisteredCommands().stream().filter(filter -> filter.getName().equalsIgnoreCase(arguments[0].substring(1))).findFirst().orElse(null);

                    //Wir überprüfen, ob iCommand einen Wert hat oder nicht
                    if (iCommand != null) {

                        //Wir rufen die Methode onCommand von ICommand.java auf, die quasi dann ausgeführt wird, wenn ein User das Kommando im Textkanal eingegeben hat

                        if (iCommand.getRoles().length > 0) {
                            try {
                                iCommand.onCommand(event.getMember(), txtChannel, message, arguments);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                iCommand.onCommand(event.getMember(), txtChannel, message, arguments);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Bot.getInstance().getMessageManager().printCommandNotFoundMessage(event.getMember(), txtChannel);
                    }
                }
            /*} else {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor("DVBL-Bot - " + member.getUser().getName());
                builder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                builder.setColor(Color.CYAN);
                builder.setTitle("Bitte schreibe deine Commands nur in <#710052952701796382>!");
                builder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", member.getUser().getEffectiveAvatarUrl());
                txtChannel.sendMessage(builder.build()).queue(exitMessage -> {
                    exitMessage.addReaction("❌").queue();

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            exitMessage.delete().queue();
                        }
                    }, TimeUnit.SECONDS.toMillis(8));
                });*/
            }
        }
    }
}