package real.entwickler.dvblbot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Created by zekro on 18.06.2017 / 11:26
 * supremeBot.audioCore
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

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
