/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 10.02.2021 @ 11:39:48
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * StopCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.MusicController;
import real.entwickler.dvblbot.utils.ICommand;

public class StopCommand extends ICommand {

    public StopCommand(String name, String usage, String description, String... roles) {
        super(name, usage, description, roles);

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        GuildVoiceState gvs;
        if ((gvs = commandSender.getVoiceState()) != null) {
            VoiceChannel vc;
            // Prüfen, ob der Bot im Channel ist
            if ((vc = gvs.getChannel()) != null) {
                MusicController controller = Bot.getInstance().playerManager.getController(vc.getGuild().getIdLong());
                // Prüfen, ob der Bot aktuell Musik spielt
                if (controller.getPlayer().getPlayingTrack() != null) {
                    AudioManager manager = vc.getGuild().getAudioManager();
                    AudioPlayer player = controller.getPlayer();

                    player.stopTrack();
                    message.addReaction("U+23F8").queue();
                } else {
                    Bot.getInstance().getMessageManager().printErrorStopCommand(commandSender, textChannel);
                }
            } else {
                Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
        }
    }
}

