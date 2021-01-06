/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * DVBLBot.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import real.entwickler.dvblbot.listener.GuildMemberJoinListener;
import real.entwickler.dvblbot.listener.GuildMemberLeaveListener;
import real.entwickler.dvblbot.listener.GuildMessageReactionAddListener;
import real.entwickler.dvblbot.manager.MessageManager;
import real.entwickler.dvblbot.utils.Property;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Bot {

    private static Bot instance;
    private Property property;
    private MessageManager messageManager;
    private JDA jda;

    public static void main(String[] args) {
        new Bot(args);
    }

    public Bot(String[] arguments) {
        instance = this;
        property = new Property();
        property.setDefaultProps();

        try {
            jda = JDABuilder.createDefault(property.get("cfg", "token"))
                    .setActivity(Activity.watching("DeineVBL auf Instagram"))
                    .setAutoReconnect(true)
                    .setStatus(OnlineStatus.ONLINE)
                    .setCompression(Compression.NONE)
                    .setBulkDeleteSplittingEnabled(false)
                    .enableIntents(Arrays.stream(GatewayIntent.values()).collect(Collectors.toList()))
                    .build().awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }

        this.messageManager = new MessageManager();
        this.jda.addEventListener(new GuildMemberJoinListener());
        this.jda.addEventListener(new GuildMemberLeaveListener());
        this.jda.addEventListener(new GuildMessageReactionAddListener());
    }

    public Guild getDVBL () {
        return jda.getGuilds().get(0);
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public JDA getJda() {
        return jda;
    }

    public Property getProperty() {
        return property;
    }

    public static Bot getInstance() {
        return instance;
    }
}
