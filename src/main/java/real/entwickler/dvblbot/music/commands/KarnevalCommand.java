package real.entwickler.dvblbot.music.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;


public class KarnevalCommand extends ICommand {

    public KarnevalCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) {
        Guild g = Bot.getInstance().getDVBL();
        if (args.length == 1) {
            Bot.getInstance().getMusicController().loadPlaylist("https://www.youtube.com/watch?v=-S2QB1emWZo&list=PL2kkyPD7bvol-81cN1J6sKour0WG1BA7j", commandSender, message, playlist -> {
                Bot.getInstance().getMusicController().getManager(g).shuffleQueue();
                Bot.getInstance().getMessageManager().printPlaylistAddedMessage(commandSender, textChannel, playlist);
            });

        }
    }
}