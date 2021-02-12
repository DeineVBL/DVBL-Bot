package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;

import java.util.Arrays;
import java.util.stream.Collectors;


public class PlayCommand extends ICommand {

    public PlayCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        if (args.length >= 2) {
            String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

            if (!(input.startsWith("http://") || input.startsWith("https://")))
                input = "ytsearch: " + input;

            Bot.getInstance().getMusicController().loadTrack(input, commandSender, message, null);
        }
    }
}
