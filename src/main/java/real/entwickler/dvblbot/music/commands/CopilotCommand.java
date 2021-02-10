package real.entwickler.dvblbot.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.managers.AudioManager;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.music.AudioLoadResult;
import real.entwickler.dvblbot.music.MusicController;
import real.entwickler.dvblbot.utils.ICommand;


public class CopilotCommand extends ICommand {

    public CopilotCommand(String name, String usage, String description, String... roles) {
        super(name, usage, description, roles);

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        if (args.length == 1) {
            GuildVoiceState gvs;
            if ((gvs = commandSender.getVoiceState()) != null) {
                VoiceChannel vc;
                if ((vc = gvs.getChannel()) != null) {
                    MusicController controller = Bot.getInstance().getPlayerManager().getController(vc.getGuild().getIdLong());
                    AudioPlayer player = controller.getPlayer();
                    AudioPlayerManager apm = Bot.getInstance().getAudioPlayerManager();
                    AudioManager manager = vc.getGuild().getAudioManager();
                    manager.openAudioConnection(vc);
                    Bot.getInstance().getMessageManager().printCoPilotSong(commandSender, textChannel);
                    apm.loadItem("https://www.youtube.com/watch?v=gr9J3BLxgeo", new AudioLoadResult(controller, "https://www.youtube.com/watch?v=gr9J3BLxgeo"));
                } else {
                    Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
                }
            } else {
                Bot.getInstance().getMessageManager().printErrorVoiceChannel(commandSender, textChannel);
            }
        } else {
            Bot.getInstance().getMessageManager().printErrorStopCommand(commandSender, textChannel);
        }
    }
}
