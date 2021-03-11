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
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import real.entwickler.dvblbot.commands.BanCommand;
import real.entwickler.dvblbot.commands.KickCommand;
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
import real.entwickler.dvblbot.radio.RadioCommand;
import real.entwickler.dvblbot.radio.commands.BigFMCommand;
import real.entwickler.dvblbot.radio.commands.RadioListCommand;
import real.entwickler.dvblbot.school.*;
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
        this.jda.addEventListener(new GuildMemberJoinListener());
        this.jda.addEventListener(new GuildMemberLeaveListener());
        this.jda.addEventListener(new GuildMessageReactionAddListener());
        this.jda.addEventListener(new GuildMessageReceivedListener());

        messageManager.printReadyMessage(EChannel.CHANGES.getChannelID());

        commandManager.registerCommand(new HelpCommand("help", "help bot", "gives you help", ""));
        commandManager.registerCommand(new HelpCommand("h", "help bot", "gives you help", ""));
        commandManager.registerCommand(new ResetCommand("reset", "resets the bot", ""));
        commandManager.registerCommand(new CheckCommand("check", "checks the modi", ""));
        commandManager.registerCommand(new KickCommand("kick", "kicks a user",""));
        commandManager.registerCommand(new BanCommand("ban", "bans a user", ""));

        commandManager.registerCommand(new WendlerCommand("wendler", "plays a wendler audio message", ""));
        commandManager.registerCommand(new WendlerCommand("michi", "plays a wendler audio message", ""));

        commandManager.registerCommand(new PlayCommand("play", "Plays a given song from youtube or spotify", ""));
        commandManager.registerCommand(new PlayCommand("p", "Plays a given song from youtube or spotify", ""));
        commandManager.registerCommand(new QueueCommand("queue", "Shows you the queue", ""));
        commandManager.registerCommand(new QueueCommand("q", "Shows you the queue", ""));
        commandManager.registerCommand(new SkipCommand("skip", "skips a queued song", ""));
        commandManager.registerCommand(new SkipCommand("s", "skips a queued song", ""));
        commandManager.registerCommand(new ClearCommand("clear", "clears a queue", ""));
        commandManager.registerCommand(new ClearCommand("c", "clears a queue", ""));
        commandManager.registerCommand(new LeaveCommand("leave", "bot leave", ""));
        commandManager.registerCommand(new PauseCommand("pause", "pauses a playing song", ""));
        commandManager.registerCommand(new ResumeCommand("resume", "resumes a paused song", ""));
        commandManager.registerCommand(new StopCommand("stop", "stops a playing song", ""));
        //commandManager.registerCommand(new LyricsCommand("lyrics", "shows the lyrics of a song", ""));
        commandManager.registerCommand(new ShuffleCommand("shuffle", "shuffles a queue", ""));
        commandManager.registerCommand(new PlayCustomSong("paulymarz", "plays the pauly marz", ""));
        commandManager.registerCommand(new CopilotCommand("copilot", "plays the copilot song", ""));
        commandManager.registerCommand(new HouseCommand("house", "plays the house playlist", ""));
        commandManager.registerCommand(new KarnevalCommand("karneval", "plays a karneval playlist", ""));
        commandManager.registerCommand(new DiscordCommand("discord", "plays the discord playlist", ""));
        commandManager.registerCommand(new DiscordCommand("dc", "plays the discord playlist", ""));
        commandManager.registerCommand(new RuskyCommand("rusky", "plays russian songs", ""));
        commandManager.registerCommand(new TimCommand("tim", "plays the playlist of tixosix", ""));
        commandManager.registerCommand(new BirthdayCommand("birthday", "plays celebration times", ""));
        commandManager.registerCommand(new BirthdayCommand("bday", "plays celebration times", ""));
        commandManager.registerCommand(new SchlagerCommand("schlager", "plays the schlager playlist", ""));
        commandManager.registerCommand(new MusicSchoolCommand("musik", "plays a pauly marz musik hit", ""));
        commandManager.registerCommand(new FaeaschtbaenklerCommand("fä", "plays fäschtbänkler songs", ""));
        commandManager.registerCommand(new FaeaschtbaenklerCommand("fäaschtbänkler", "plays fäschtbänkler songs", ""));
        commandManager.registerCommand(new MoveCommand("move", "Moves the given song to a given index in the queue", ""));
        commandManager.registerCommand(new LoopCommand("loop", "loops a current song", ""));
        commandManager.registerCommand(new EarrapeCommand("earrape", "makes a song to an earrape song", ""));
        commandManager.registerCommand(new EarrapeCommand("er", "makes a song to an earrape song", ""));
        commandManager.registerCommand(new BassboostCommand("bassboost", "plays a song in bassboosted", ""));
        commandManager.registerCommand(new BassboostCommand("bb", "plays a song in bassboosted", ""));
        commandManager.registerCommand(new JumpCommand("jump", "jumps to a song in a queue", ""));
        commandManager.registerCommand(new RemoveCommand("remove", "removes a song in a queue", ""));
        commandManager.registerCommand(new SetVolumeCommand("setvolume", "sets the player volume", ""));
        commandManager.registerCommand(new SetVolumeCommand("sv", "sets the player volume", ""));
        commandManager.registerCommand(new AchtDAudioCommand("8d", "plays 8d audio music", ""));
        commandManager.registerCommand(new AchtDAudioCommand("achtd", "plays 8d audio music", ""));
        commandManager.registerCommand(new AchtDAudioCommand("8daudio", "plays 8d audio music", ""));

        commandManager.registerCommand(new RadioCommand("r", "plays a radio sender", ""));
        commandManager.registerCommand(new RadioCommand("radio", "plays a radio sender", ""));
        commandManager.registerCommand(new RadioListCommand("rl", "shows all available radio sender", ""));
        commandManager.registerCommand(new RadioListCommand("radiolist", "shows all available radio sender", ""));
        commandManager.registerCommand(new BigFMCommand("bigfm", "plays big fm mashup radio", ""));
        commandManager.registerCommand(new BigFMCommand("bf", "plays big fm mashup radio", ""));

        commandManager.registerCommand(new BBBCommand("bbb", "shows the bbb links", ""));
        commandManager.registerCommand(new UploadCommand("upload", "shows the upload links", ""));
        commandManager.registerCommand(new UploadCommand("u", "shows the upload links", ""));
        commandManager.registerCommand(new LinkCommand("links", "shows you links for school", ""));
        commandManager.registerCommand(new LinkCommand("l", "shows you links for school", ""));
        commandManager.registerCommand(new FileinfoCommand("fileinfo", "shows you how to name a file", ""));
        commandManager.registerCommand(new FileinfoCommand("fi", "shows you how to name a file", ""));

        commandManager.registerCommand(new MSFSCommand("msfs", "shows useful msfs links", ""));

        commandManager.registerCommand(new BeleidigungCommand("ficken", "kicks a user for a beleidigung", ""));

        MusicController.guild = getDVBL();

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
