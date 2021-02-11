package real.entwickler.dvblbot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;


public class PlayCustomSong extends ICommand {

    public PlayCustomSong(String name, String description, String... roles) {
        super(name, description, roles);

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel txtChannel = event.getChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
    }

    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        if (args.length == 1) {
            Bot.getInstance().getMusicController().loadTrack("https://schulbox.bildung-rp.de/index.php/s/cprCRtcN3kcfHMJ/download?path=%2FMu_Pam%2F6.%20Wochenplan%2010.02.&files=Karlheinz%20Stockhausen%20Das%20zerbrochene%20Lied%20der%20Deutschen%20(Ohne%20Kommentar).mp3&downloadStartSecret=vvl3bi9coho", commandSender, message);
        }
    }
}
