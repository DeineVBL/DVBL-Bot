/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 02.03.2021 @ 20:20:46
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MusicSchoolCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;


public class MusicSchoolCommand extends ICommand {

    public MusicSchoolCommand(String name, String description, String... roles) {
        super(name, description, roles);

    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild g = Bot.getInstance().getDVBL();
        Bot.getInstance().getMusicController().loadTrack("https://s3.itslearning.com/fetch?id=9301%2F5c25-1d69-46bc-9d00-a0fb30ecbbbe&version=1&expires=2021-02-25T16%3A47%3A13Z&isDownloadRequest=0&s3Url=https%3A%2F%2Fs3.eu-central-1.amazonaws.com%2Fprod.eu-central-1.filerepo%2F9301%2F5c25-1d69-46bc-9d00-a0fb30ecbbbe%3FX-Amz-Expires%3D9000%26response-cache-control%3Dmax-age%253D9000%26response-content-disposition%3Dinline%253B%2520filename%253D%252230%252520Track%25252030.mp3%2522%253Bfilename%252A%253Dutf-8%2527%252730%252520Track%25252030.mp3%253B%26response-content-type%3Daudio%252Fmpeg%26X-Amz-Algorithm%3DAWS4-HMAC-SHA256%26X-Amz-Credential%3DAKIAIQ6TLTXZ2ESTRSDA%2F20210225%2Feu-central-1%2Fs3%2Faws4_request%26X-Amz-Date%3D20210225T141712Z%26X-Amz-SignedHeaders%3Dhost%26X-Amz-Signature%3Dd8525791fdb58ae96cbc6bcc12f21bb74e27b1ebf915c1dbae8bed3257d9a8df&sign=41623DHQC4y0qbDwuhv9d2csPZ0olNtyzrnpvjl%2BNP8%3D", commandSender, message, null);
    }
}
