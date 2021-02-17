/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * AudioInfo.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class AudioInfo {

    private final AudioTrack TRACK;
    private final Member AUTHOR;
    private final TextChannel CHANNEL;

    /**
     * Erstellt eine Instanz der Klasse AudioInfo.
     * @param track AudioTrack
     * @param author Member, der den Track eingereiht hat
     * @param channel
     */
    public AudioInfo(AudioTrack track, Member author, TextChannel channel) {
        this.TRACK = track;
        this.AUTHOR = author;
        CHANNEL = channel;
    }

    public AudioTrack getTrack() {
        return TRACK;
    }

    public Member getAuthor() {
        return AUTHOR;
    }

    public TextChannel getChannel() {
        return CHANNEL;
    }
}
