/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 09.03.2021 @ 12:45:58
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * SetVolumeCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;

public class SetVolumeCommand extends ICommand {

    public SetVolumeCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild guild = Bot.getInstance().getDVBL();

        GuildVoiceState gvs;
        if ((gvs = commandSender.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = gvs.getChannel()) != null) {
                Guild g = Bot.getInstance().getDVBL();
                AudioTrack audioTrack = Bot.getInstance().getMusicController().getPlayer(g).getPlayingTrack();
                AudioManager manager = vc.getGuild().getAudioManager();

                if (manager.isConnected()) {

                    Integer volume = Integer.parseInt(args[1]);

                    if (commandSender.getId().equals("404301583027798032")) {
                        if (volume > 100) {
                            textChannel.sendMessage("Nein.").queue();
                            return;
                        }
                    }

                    if (commandSender.getId().equals("697772960261472282")) {
                        if (volume > 100) {
                            textChannel.sendMessage("Nein.").queue();
                            return;
                        }
                    }

                    if (!commandSender.isOwner()) {
                        if (volume > 400) {
                            textChannel.sendMessage("Nein.").queue();
                            return;
                        }
                    }

                    if (args[1].equals("default")) {
                        Bot.getInstance().getMusicController().getPlayer(guild).setVolume(50);
                    } else {
                        Bot.getInstance().getMusicController().getPlayer(guild).setVolume(Integer.parseInt(args[1]));
                    }
                    message.addReaction("U+1F50A").queue();
                } else {
                    Bot.getInstance().getMessageManager().printBotErrorVoiceChannel(commandSender, textChannel);
                }
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
        }
    }
}
