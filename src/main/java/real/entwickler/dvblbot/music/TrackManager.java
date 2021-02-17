/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * TrackManager.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.*;
import real.entwickler.dvblbot.Bot;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer PLAYER;
    private final Queue<AudioInfo> queue;


    /**
     * Erstellt eine Instanz der Klasse TrackManager.
     *
     * @param player
     */
    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Reiht den übergebenen Track in die Queue ein.
     *
     * @param track  AudioTrack
     * @param author Member, der den Track eingereiht hat
     */
    public void queue(AudioTrack track, AudioPlaylist playlist, Member author, TextChannel textChannel, Message message) {
        String identifier = message.getContentRaw();
        AudioInfo info = new AudioInfo(track, author, textChannel);
        queue.add(info);

        if (PLAYER.getPlayingTrack() == null) {
            if (!identifier.contains("list")) {
                PLAYER.playTrack(track);
                Bot.getInstance().getMessageManager().printPlayingSongMessage(track, author, textChannel);
            } else {
                if (!identifier.contains("list")) {
                    PLAYER.playTrack(track);
                    Bot.getInstance().getMessageManager().printPlaylistAddedMessage(author, textChannel, playlist);
                }
            }
        } else {
            if (identifier.contains("list")) {
                Bot.getInstance().getMessageManager().printPlaylistAddedMessage(author, textChannel, playlist);
            } else {
                String contentRaw = message.getContentRaw();

                if (!contentRaw.equalsIgnoreCase(".karneval") && !contentRaw.equalsIgnoreCase(".house") && !contentRaw.equalsIgnoreCase(".discord")) {
                    Bot.getInstance().getMessageManager().printSongAddedQueueMessage(track, author, textChannel);
                }
            }
        }
    }

    /**
     * Returnt die momentane Queue als LinkedHashSet.
     *
     * @return Queue
     */
    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    public Queue<AudioInfo> getQueue2() {
        return queue;
    }

    /**
     * Returnt AudioInfo des Tracks aus der Queue.
     *
     * @param track AudioTrack
     * @return AudioInfo
     */
    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    /**
     * Leert die gesammte Queue.
     */
    public void purgeQueue() {
        queue.clear();
    }

    /**
     * Shufflet die momentane Queue.
     */
    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }

    /**
     * PLAYER EVENT: TRACK STARTET
     * Wenn Einreiher nicht im VoiceChannel ist, wird der Player gestoppt.
     * Sonst connectet der Bot in den Voice Channel des Einreihers.
     *
     * @param player AudioPlayer
     * @param track  AudioTrack
     */
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();

        if (vChan == null)
            player.stopTrack();
        else
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
    }

    /**
     * PLAYER EVENT: TRACK ENDE
     * Wenn die Queue zuende ist, verlässt der Bot den Audio Channel.
     * Sonst wird der nächste Track in der Queue wiedergegeben.
     *
     * @param player
     * @param track
     * @param endReason
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Guild g = queue.poll().getAuthor().getGuild();

        if (queue.isEmpty()) {
            g.getAudioManager().closeAudioConnection();
        } else {
            AudioInfo nextTrack = queue.element();
            player.playTrack(nextTrack.getTrack());

        }

    }

}
