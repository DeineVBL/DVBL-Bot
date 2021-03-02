/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 17.02.2021 @ 16:19:55
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MoveCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.utils.ICommand;

public class MoveCommand extends ICommand {

    public MoveCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
//        if (args.length == 3) {
//            int toMove = 0, moveIndex = 0;
//            try {
//                 toMove = Integer.parseInt(args[1]);
//                 moveIndex = Integer.parseInt(args[2]);
//            } catch (NumberFormatException ec) {
//                //TOOO Sent message --> User typed words instead of numbers
//                return;
//            }
//            TrackManager manager = Bot.getInstance().getMusicController().getManager(message.getGuild());
//
//            if (toMove <= manager.getQueue2().size() && toMove > 2) {
//                if (moveIndex <= manager.getQueue2().size() && moveIndex > 1) {
//
//                } else{
//                    //TODO: Die "moveIndex" ist = 1 oder befindet sich außerhalb der Queue
//                }
//            } else {
//                //TODO: Die "toMove" Stelle befindet sich außerhalb der Queue
//            }
//        }

    }
}




/*







 if (Bot.getInstance().isPlaying()) {
            //return send(error(event.translate("phrases.notplaying.title"), event.translate("phrases.notplaying.description")));
        }

        if (args.length != 2) {
            //return sendHelp();
        }

        int songPos = Integer.parseInt(args[1]);
        int wantPos = Integer.parseInt(args[2]);

        if (songPos > Bot.getInstance().getMusicController().getManager(Bot.getInstance().getDVBL()).getTrackQueue().size() || wantPos > Bot.getInstance().getMusicController().getManager(Bot.getInstance().getDVBL()).getTrackQueue().size() || songPos < 1 || wantPos < 1) {
            //return send(error(event.translate("phrases.invalid"), event.translate("phrases.invalidnumbers.description")));
        }

        if (songPos == wantPos) {
            //return send(error(event.translate("phrases.error"), event.translate("phrases.error.samenumbers")));
        }

        LinkedList<AudioTrack> trackQueue = (LinkedList<AudioTrack>) Bot.getInstance().getMusicController().getManager(Bot.getInstance().getDVBL()).getTrackQueue();

        int songPosIndex = songPos - 1;
        int wantPosIndex = wantPos - 1;

        AudioTrack preSave = trackQueue.get(songPosIndex);
        trackQueue.remove(songPosIndex);
        trackQueue.add(wantPosIndex, preSave);

        //return send(success(event.translate("phrases.success"), String.format(event.translate("command.move"), preSave.getInfo().title, wantPos)));








 */