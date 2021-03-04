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
        Bot.getInstance().getMusicController().loadTrack("https://s3.itslearning.com/fetch?id=ef4f%2Fbb27-e303-491f-aa61-aa7c5924afba&version=1&expires=2021-03-04T15%3A08%3A22Z&isDownloadRequest=0&s3Url=https%3A%2F%2Fs3.eu-central-1.amazonaws.com%2Fprod.eu-central-1.filerepo%2Fef4f%2Fbb27-e303-491f-aa61-aa7c5924afba%3FX-Amz-Expires%3D9001%26response-cache-control%3Dmax-age%253D9000%26response-content-disposition%3Dinline%253B%2520filename%253D%2522Das%252520Lied%252520der%252520Partei_Track%25252031.mp3%2522%253Bfilename%252A%253Dutf-8%2527%2527Das%252520Lied%252520der%252520Partei_Track%25252031.mp3%253B%26response-content-type%3Daudio%252Fmpeg%26X-Amz-Algorithm%3DAWS4-HMAC-SHA256%26X-Amz-Credential%3DAKIAIQ6TLTXZ2ESTRSDA%2F20210304%2Feu-central-1%2Fs3%2Faws4_request%26X-Amz-Date%3D20210304T123821Z%26X-Amz-SignedHeaders%3Dhost%26X-Amz-Signature%3D7005fc05b44cc6456578154179333d7630d3974426f7c63d604d11c57a8e6a36&sign=KvL4B8lf5cA1eA%2BgyZxCryWlaN7WJPbHTQGeSbtl82g%3D", commandSender, message, null);
    }
}
