package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sun.imageio.plugins.common.BogusColorSpace;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.AudioLoadResult;
import real.entwickler.dvblbot.music.MusicController;
import real.entwickler.dvblbot.utils.EmbedMessage;
import real.entwickler.dvblbot.utils.ICommand;


public class PlayCommand extends ICommand {

    public PlayCommand(String name, String usage, String description, String... roles) {
        super(name, usage, description, roles);

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        if (args.length >= 2) {
            GuildVoiceState gvs;
            if ((gvs = commandSender.getVoiceState()) != null) {
                VoiceChannel vc;
                if ((vc = gvs.getChannel()) != null) {
                    MusicController controller = Bot.getInstance().playerManager.getController(vc.getGuild().getIdLong());
                    AudioPlayer player = controller.getPlayer();
                    AudioPlayerManager apm = Bot.getInstance().audioPlayerManager;
                    AudioManager manager = vc.getGuild().getAudioManager();
                    manager.openAudioConnection(vc);

                    if (args.length == 2) {
                        String url = args[1];

                        if (!url.startsWith("http")) {
                            url = "ytsearch: " + url;
                        }
                        apm.loadItem(url, new AudioLoadResult(controller, url) {
                            @Override
                            public void trackLoaded(AudioTrack track) {
                                super.trackLoaded(track);
                                String trackTitle = track.getInfo().title;
                                String embedTitle = "Link";
                                EmbedBuilder messageEmbed = new EmbedMessage("Now playing [" + embedTitle + "]", "Co-Pilot - " + commandSender.getEffectiveName(), trackTitle, null, null).raw(track);
                                textChannel.sendMessage(messageEmbed.build()).queue(message1 -> message1.addReaction("U+1F3B6").queue());
                            }

                            @Override
                            public void noMatches() {
                                textChannel.sendMessage("Ich habe kein Lied gefunden.").queue();

                            }

                            @Override
                            public void loadFailed(FriendlyException e) {
                                textChannel.sendMessage("lol kaputt").queue();
                            }
                        });
                    } else {
                        StringBuilder strBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            strBuilder.append(args[i]).append(" ");
                        }

                        String url = "ytsearch: " + strBuilder.toString();
                        apm.loadItem(url, new AudioLoadResult(controller, url) {
                            @Override
                            public void trackLoaded(AudioTrack track) {
                                super.trackLoaded(track);
                                textChannel.sendMessage("Ich spiele nun `" + track.getSourceManager() + "` im Sprachkanal: " + vc.getName()).queue();
                            }

                            @Override
                            public void noMatches() {
                                textChannel.sendMessage("Ich habe kein Lied gefunden.").queue();
                            }

                            @Override
                            public void loadFailed(FriendlyException e) {
                                textChannel.sendMessage("lol kaputt").queue();
                            }

                            @Override
                            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                                textChannel.sendMessage("lol").queue();
                            }
                        });
                    }
                } else {
                    Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
                }
            } else {
                Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorPlayCommand(commandSender, textChannel);
        }
    }
}
