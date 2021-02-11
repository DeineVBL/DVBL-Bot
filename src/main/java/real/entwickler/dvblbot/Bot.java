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

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import real.entwickler.dvblbot.enums.EChannel;
import real.entwickler.dvblbot.listener.GuildMemberJoinListener;
import real.entwickler.dvblbot.listener.GuildMemberLeaveListener;
import real.entwickler.dvblbot.listener.GuildMessageReactionAddListener;
import real.entwickler.dvblbot.listener.GuildMessageReceivedListener;
import real.entwickler.dvblbot.manager.CommandManager;
import real.entwickler.dvblbot.manager.MessageManager;
import real.entwickler.dvblbot.music.MusicController;
import real.entwickler.dvblbot.music.PlayCustomSong;
import real.entwickler.dvblbot.music.commands.*;
import real.entwickler.dvblbot.utils.Property;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Bot {

    private static Bot instance;
    private Property property;
    private MessageManager messageManager;
    private MusicController musicController;
    private JDA jda;
    private CommandManager commandManager;

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
        this.commandManager = new CommandManager();
        this.musicController = new MusicController();
        MusicController.guild = getDVBL();
        this.jda.addEventListener(new GuildMemberJoinListener());
        this.jda.addEventListener(new GuildMemberLeaveListener());
        this.jda.addEventListener(new GuildMessageReactionAddListener());
        this.jda.addEventListener(new GuildMessageReceivedListener());

        messageManager.printReadyMessage(EChannel.CHANGES.getChannelID());

        commandManager.registerCommand(new PlayCommand("play", "play <Songlink>", "Plays a given song from youtube or spotify"));
        commandManager.registerCommand(new PlayCommand("p", "play <Songlink>", "Plays a given song from youtube or spotify"));
        commandManager.registerCommand(new StopCommand("stop", "stop Song", "stops a playing song", ""));
        commandManager.registerCommand(new QueueCommand("queue", "Shows you the queue", ""));
        //commandManager.registerCommand(new HelpCommand("help", "help bot", "gives you help", ""));
        commandManager.registerCommand(new CopilotCommand("copilot", "CoPilot song", "plays the copilot song"));
        commandManager.registerCommand(new LeaveCommand("leave", "bot leave", "make bot leave vc"));
        commandManager.registerCommand(new PlayCustomSong("paulymarz", "plays great song", "plays the pauly marz"));
        //commandManager.registerCommand(new Music("m", "play <Songlink>", "Plays a given song from youtube or spotify"));

        Scanner scanner = new Scanner(System.in);

        if (new Scanner(System.in).nextLine().equalsIgnoreCase("stop")) {
            messageManager.printStopMessage(EChannel.CHANGES.getChannelID());
        }
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public MusicController getMusicController() {
        return musicController;
    }

    public Guild getDVBL() {
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
