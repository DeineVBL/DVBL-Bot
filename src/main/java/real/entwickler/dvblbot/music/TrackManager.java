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
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.*;
import org.apache.hc.core5.http.ParseException;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.commands.SetVolumeCommand;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer PLAYER;
    //private final Queue<AudioInfo> queue;
    private final ArrayList<AudioInfo> queue2;

    private AudioTrack currentTrack;

    /**
     * Erstellt eine Instanz der Klasse TrackManager.
     *
     * @param player
     */
    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        //this.queue = new LinkedBlockingQueue<>();
        this.queue2 = new ArrayList<AudioInfo>();
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
        //queue.add(info);
        queue2.add(info);

        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
            currentTrack = track;

            if (!identifier.contains("list")) {
                Bot.getInstance().getMessageManager().handlePlayingSongMessage(track, author, textChannel);
            } else {
                Bot.getInstance().getMessageManager().printPlaylistAddedMessage(author, textChannel, playlist);
            }
        } else {
            if (identifier.contains("list")) {

            } else {
                String contentRaw = message.getContentRaw();

                if (!contentRaw.equalsIgnoreCase(".karneval") && !contentRaw.equalsIgnoreCase(".house") && !contentRaw.equalsIgnoreCase(".discord") && !contentRaw.equalsIgnoreCase(".rusky") && !contentRaw.equalsIgnoreCase(".tim") && !contentRaw.equalsIgnoreCase(".dc") && !contentRaw.equalsIgnoreCase(".schlager") && !contentRaw.equalsIgnoreCase(".fä") && !contentRaw.equalsIgnoreCase(".fäaschtbänkler") && !contentRaw.equalsIgnoreCase(".bigfm") && !contentRaw.equalsIgnoreCase("bf") && !contentRaw.equalsIgnoreCase("rl") && !contentRaw.equalsIgnoreCase("radiolist")) /*&& !contentRaw.equalsIgnoreCase("playlist")) */{
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
    /*public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }*/
    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue2);
    }

    /*public Queue<AudioInfo> getQueue2() {
        return queue;
    }*/

    public Queue<AudioInfo> getQueue2() {
        return new LinkedBlockingQueue<>(queue2);
    }

    public ArrayList<AudioInfo> getNewQueue() {
        return queue2;
    }

    /*public ArrayList<AudioInfo> getQueueAsArray() {
        return new ArrayList<>(queue);
    }*/

    /**
     * Returnt AudioInfo des Tracks aus der Queue.
     *
     * @param track AudioTrack
     * @return AudioInfo
     */
    public AudioInfo getInfo(AudioTrack track) {
        /*return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);*/
        return queue2.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    /**
     * Leert die gesammte Queue.
     */
    public void purgeQueue() {
        //queue.clear();
        queue2.clear();
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
        //queue.addAll(cQueue);
        queue2.addAll(cQueue);
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
        //AudioInfo info = queue.element();
        AudioInfo info = queue2.get(0);
        VoiceChannel vChan = Objects.requireNonNull(info.getAuthor().getVoiceState()).getChannel();

        if (vChan == null)
            player.stopTrack();
        else {
            if (Bot.getInstance().getMusicController().isEarrapeMode()) {
                player.setVolume(2147483647);
            }
            if (!Bot.getInstance().getMusicController().isEarrapeMode()) {
                SetVolumeCommand setVolumeCommand;
            }
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
            info.getAuthor().getGuild().getAudioManager().setSelfDeafened(true);
        }
        currentTrack = track;
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
    @SneakyThrows
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        //Guild g = Objects.requireNonNull(queue.poll()).getAuthor().getGuild();
        Guild g = Objects.requireNonNull(queue2.remove(0)).getAuthor().getGuild();
        Message latestPlayingMessage = Bot.getInstance().getMessageManager().getLatestPlayingMessage();


       // if (queue.isEmpty()) {
       if (queue2.isEmpty()) {
            if (Bot.getInstance().getMusicController().isLoopMode()) {
                player.playTrack(currentTrack.makeClone());
            } else {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (player.getPlayingTrack() == null) {
                            Bot.getInstance().getMessageManager().printInactivityTimeoutMessage(Objects.requireNonNull(latestPlayingMessage.getMember()), latestPlayingMessage.getTextChannel());
                            g.getAudioManager().closeAudioConnection();
                        }
                    }
                }, TimeUnit.SECONDS.toMillis(150));
            }
        } else {
            if (Bot.getInstance().getMusicController().isLoopMode()) {
                player.playTrack(currentTrack.makeClone());
            }
            //AudioInfo nextTrack = queue.element();
            AudioInfo nextTrack = queue2.get(0);
            player.playTrack(nextTrack.getTrack());
            Bot.getInstance().getMessageManager().handlePlayingSongMessage(nextTrack.getTrack(), nextTrack.getAuthor(), nextTrack.getChannel());
        }
    }
}
