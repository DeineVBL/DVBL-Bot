/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * Bot.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot;

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
import real.entwickler.dvblbot.school.BBBCommand;
import real.entwickler.dvblbot.utils.GeniusClient;
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

    private GeniusClient geniusClient;

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
        this.geniusClient = new GeniusClient(property.get("cfg", "genius-token"));
        MusicController.guild = getDVBL();
        this.jda.addEventListener(new GuildMemberJoinListener());
        this.jda.addEventListener(new GuildMemberLeaveListener());
        this.jda.addEventListener(new GuildMessageReactionAddListener());
        this.jda.addEventListener(new GuildMessageReceivedListener());

        messageManager.printReadyMessage(EChannel.CHANGES.getChannelID());

        commandManager.registerCommand(new PlayCommand("play", "Plays a given song from youtube or spotify", ""));
        commandManager.registerCommand(new PlayCommand("p", "Plays a given song from youtube or spotify", ""));
        commandManager.registerCommand(new QueueCommand("queue", "Shows you the queue", ""));
        commandManager.registerCommand(new QueueCommand("q", "Shows you the queue", ""));
        //commandManager.registerCommand(new HelpCommand("help", "help bot", "gives you help", ""));
        commandManager.registerCommand(new CopilotCommand("copilot", "plays the copilot song", ""));
        commandManager.registerCommand(new LeaveCommand("leave", "bot leave", ""));
        commandManager.registerCommand(new PlayCustomSong("paulymarz", "plays the pauly marz", ""));
        commandManager.registerCommand(new SkipCommand("skip", "skips a queued song", ""));
        commandManager.registerCommand(new SkipCommand("s", "skips a queued song", ""));
        commandManager.registerCommand(new ShuffleCommand("shuffle", "shuffles a queue", ""));
        commandManager.registerCommand(new ResumeCommand("resume", "resumes a paused song", ""));
        commandManager.registerCommand(new PauseCommand("pause", "pauses a playing song", ""));
        commandManager.registerCommand(new ClearCommand("clear", "clears a queue", ""));
        commandManager.registerCommand(new HouseCommand("house", "plays the house playlist", ""));
        commandManager.registerCommand(new KarnevalCommand("karneval", "plays a karneval playlist", ""));
        commandManager.registerCommand(new DiscordCommand("discord", "plays the discord playlist", ""));
        commandManager.registerCommand(new StopCommand("stop", "stops a playing song", ""));
        commandManager.registerCommand(new RuskyCommand("rusky", "plays russian songs", ""));
        commandManager.registerCommand(new LyricsCommand("lyrics", "shows the lyrics of a song", ""));
        commandManager.registerCommand(new TimCommand("tim", "plays the playlist of tixosix", ""));
        commandManager.registerCommand(new BBBCommand("bbb", "shows the bbb links", ""));


        Scanner scanner = new Scanner(System.in);

        if (new Scanner(System.in).nextLine().equalsIgnoreCase("stop")) {
            messageManager.printStopMessage(EChannel.CHANGES.getChannelID());
        }


    }

    public GeniusClient getGeniusClient() {
        return geniusClient;
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
