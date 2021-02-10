package real.entwickler.dvblbot.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class ICommand {

    private final String name;
    private final String usage;
    private final String description;
    private final String[] roles;

    public ICommand(String name, String usage, String description, String... roles) {
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.roles = roles;
    }

    public abstract void onCommand (Member commandSender, TextChannel textChannel, Message message, String[] args);

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public String[] getRoles() {
        return roles;
    }
}
