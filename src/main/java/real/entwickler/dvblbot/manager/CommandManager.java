package real.entwickler.dvblbot.manager;


import real.entwickler.dvblbot.utils.ICommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private List<ICommand> registeredCommands;

    public CommandManager() {
        this.registeredCommands = new ArrayList<>();
    }

    public void registerCommand (ICommand iCommand) {
        this.registeredCommands.add(iCommand);
    }

    public List<ICommand> getRegisteredCommands() {
        return registeredCommands;
    }
}
